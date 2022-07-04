package org.roy.trb.tst.credit.line.services;

import static org.roy.trb.tst.credit.line.constants.BusinessRulesConstants.MAX_NUMBER_OF_FAILED_ATTEMPTS;
import static org.roy.trb.tst.credit.line.constants.Messages.SALES_AGENT_MSG;
import static org.roy.trb.tst.credit.line.enums.CreditLineStatus.NONE;
import static org.roy.trb.tst.credit.line.enums.CreditLineStatus.REJECTED;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.roy.trb.tst.credit.line.entities.CreditLineRequestRecord;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.roy.trb.tst.credit.line.exceptions.RejectedCreditLineException;
import org.roy.trb.tst.credit.line.models.daos.CreditLineRequestRecordDao;
import org.roy.trb.tst.credit.line.models.requests.PostRequestCreditLineRequestBody;
import org.roy.trb.tst.credit.line.models.responses.PostRequestCreditLineResponseBody;
import org.roy.trb.tst.credit.line.repositories.CreditLineRequestRepository;
import org.roy.trb.tst.credit.line.services.mappers.CreditLineRequestMapper;
import org.roy.trb.tst.credit.line.services.strategies.credit.status.CreditRequestStrategy;
import org.roy.trb.tst.credit.line.services.strategies.founding.type.CreditLineCalculationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreditLineServiceImpl implements CreditLineService {

  // Dependency Injection
  private final CreditLineRequestMapper mapper;
  private final CreditLineRequestRepository creditLineRequestsRepository;
  private final RateLimitService rateLimitService;

  /** {@inheritDoc} */
  @Override
  public PostRequestCreditLineResponseBody requestCreditLine(
      UUID customerId,
      PostRequestCreditLineRequestBody postRequestCreditLineRequestBody,
      FoundingType foundingType) {

    CreditLineCalculationStrategy creditLineCalculationStrategy =
        CreditLineCalculationStrategy.getCreditLineCalculationStrategy(foundingType);

    CreditLineRequestRecordDao lastCreditLineRecord = getLastCreditLineRecord(customerId);

    CreditRequestStrategy creditRequestStrategy =
        CreditRequestStrategy.getCreditRequestStrategy(lastCreditLineRecord.getCreditLineStatus());

    var requesterFinancialData =
        mapper.mapToRequesterFinancialData(postRequestCreditLineRequestBody, customerId);

    CreditLineRequestRecordDao processedCreditLineRequest =
        creditRequestStrategy.processCreditLineRequest(
            creditLineCalculationStrategy, requesterFinancialData, lastCreditLineRecord);

    creditLineRequestsRepository.save(
        mapper.mapToCreditLineRequestEntity(processedCreditLineRequest));

    return getThePostRequestCreditLineResponseBody(processedCreditLineRequest);
  }

  /**
   * Check if the user with the given customerId has already made any credit line request
   *
   * @param customerId query filter
   * @return status of the request
   */
  private CreditLineRequestRecordDao getLastCreditLineRecord(UUID customerId) {

    return mapper.mapToCreditLineRequestRecordDao(
        creditLineRequestsRepository
            .findById(customerId)
            .orElse(
                CreditLineRequestRecord.builder()
                    .customerId(customerId)
                    .creditLineStatus(NONE.name())
                    .attempts(0)
                    .build()));
  }

  /**
   * Get the api response body based on the status of the processed credit line request
   *
   * @param processedCreditLineRequest process credit line request
   * @return api response body
   */
  private PostRequestCreditLineResponseBody getThePostRequestCreditLineResponseBody(
      CreditLineRequestRecordDao processedCreditLineRequest) {

    if (REJECTED.equals(processedCreditLineRequest.getCreditLineStatus())) {

      rateLimitService.setRateLimitForRejectedCredit(processedCreditLineRequest.getCustomerId());

      String rejectedCreditLineMessage =
          processedCreditLineRequest.getAttempts() > MAX_NUMBER_OF_FAILED_ATTEMPTS
              ? SALES_AGENT_MSG
              : StringUtils.EMPTY;

      throw new RejectedCreditLineException(rejectedCreditLineMessage);
    }

    rateLimitService.setRateLimitForAcceptedCredit(processedCreditLineRequest.getCustomerId());

    return mapper.mapToRequestCreditLineResponseBody(processedCreditLineRequest);
  }
}
