package org.roy.trb.tst.credit.line.services;

import static org.roy.trb.tst.credit.line.constants.Messages.SALES_AGENT_MSG;
import static org.roy.trb.tst.credit.line.enums.CreditLineStatus.REJECTED;
import static org.roy.trb.tst.credit.line.enums.FoundingType.SME;
import static org.roy.trb.tst.credit.line.enums.FoundingType.STARTUP;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.roy.trb.tst.credit.line.entities.CreditLineRequestRecords;
import org.roy.trb.tst.credit.line.enums.CreditLineStatus;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.roy.trb.tst.credit.line.exceptions.RejectedCreditLineException;
import org.roy.trb.tst.credit.line.models.RequesterFinancialData;
import org.roy.trb.tst.credit.line.models.requests.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.responses.CreditLineStatusResponse;
import org.roy.trb.tst.credit.line.models.responses.PostRequestCreditLineResponseBody;
import org.roy.trb.tst.credit.line.repositories.CreditLineRequestRepository;
import org.roy.trb.tst.credit.line.services.mappers.CreditLineRequestMapper;
import org.roy.trb.tst.credit.line.services.strategies.founding.type.FoundingTypeStrategy;
import org.roy.trb.tst.credit.line.services.strategies.founding.type.SmeRequesterStrategy;
import org.roy.trb.tst.credit.line.services.strategies.founding.type.StartUpRequesterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreditLineServiceImpl implements CreditLineService {

  public static final Integer MAX_NUMBER_OF_FAILED_ATTEMPTS = 3;

  private final CreditLineRequestMapper mapper;

  private final CreditLineRequestRepository creditLineRequestsRepository;

  @Setter
  @Value("${ratio.monthly-revenue}")
  private Integer monthlyRevenueRatio;

  @Setter
  @Value("${ratio.cash-balance}")
  private Integer cashBalanceRatio;

  private FoundingTypeStrategy foundingTypeStrategy;

  /** {@inheritDoc} */
  @Override
  public PostRequestCreditLineResponseBody validateCreditLine(
      UUID customerId, CreditLineRequest creditLineRequest, FoundingType foundingType) {

    // "requestedDate" attribute must comply with real data
    ZonedDateTime requestedDate = Instant.now().atZone(ZoneOffset.UTC);
    creditLineRequest.setRequestedDate(requestedDate);

    var requesterFinancialData = mapper.mapToRequesterFinancialData(creditLineRequest);

    setFoundingTypeStrategy(foundingType);

    Optional<CreditLineRequestRecords> optionalCreditLineRequest =
        creditLineRequestsRepository.findById(customerId);

    if (optionalCreditLineRequest.isPresent()) {

      CreditLineStatusResponse existentCreditLineResponse =
          processExistentCreditLineResponse(
              requesterFinancialData, requestedDate, optionalCreditLineRequest.get());

      return getValidateCreditApiResponse(existentCreditLineResponse);
    }

    var creditLineStatusResponse =
        processNewCreditLineResponse(customerId, requesterFinancialData, requestedDate);

    return getValidateCreditApiResponse(creditLineStatusResponse);
  }

  @Override
  public CreditLineStatus getCustomerCreditLineStatus(UUID customerId) {

    CreditLineRequestRecords entity =
        creditLineRequestsRepository
            .findById(customerId)
            .orElse(CreditLineRequestRecords.builder().creditLineStatus(REJECTED.name()).build());

    return CreditLineStatus.valueOf(entity.getCreditLineStatus());
  }

  private PostRequestCreditLineResponseBody getValidateCreditApiResponse(
      CreditLineStatusResponse creditLineStatusResponse) {

    if (creditLineStatusResponse.getCreditLineStatus().equals(REJECTED)) {
      throw new RejectedCreditLineException();
    }

    return mapper.mapToCreditLineApiResponse(creditLineStatusResponse);
  }

  private CreditLineStatusResponse processNewCreditLineResponse(
      UUID customerId, RequesterFinancialData requesterFinancialData, ZonedDateTime requestedDate) {
    BigDecimal approvedCredit =
        foundingTypeStrategy.getCreditLine(requesterFinancialData).orElse(BigDecimal.ZERO);

    var creditLineStatusResponse =
        CreditLineStatusResponse.builder()
            .customerId(customerId)
            .acceptedCreditLine(approvedCredit)
            .attempts(0)
            .build();

    saveOrUpdateCreditLineResponse(creditLineStatusResponse, approvedCredit, requestedDate);

    return creditLineStatusResponse;
  }

  private CreditLineStatusResponse processExistentCreditLineResponse(
      RequesterFinancialData requesterFinancialData,
      ZonedDateTime requestedDate,
      CreditLineRequestRecords creditLineRequest) {
    var existentCreditLineResponse = mapper.mapToCreditLineStatusResponse(creditLineRequest);

    if (REJECTED.equals(existentCreditLineResponse.getCreditLineStatus())) {

      if (existentCreditLineResponse.getAttempts() > MAX_NUMBER_OF_FAILED_ATTEMPTS) {
        throw new RejectedCreditLineException(SALES_AGENT_MSG);
      }

      BigDecimal approvedCredit =
          foundingTypeStrategy.getCreditLine(requesterFinancialData).orElse(BigDecimal.ZERO);

      saveOrUpdateCreditLineResponse(existentCreditLineResponse, approvedCredit, requestedDate);
    }

    saveOrUpdateCreditLineResponse(
        existentCreditLineResponse,
        existentCreditLineResponse.getAcceptedCreditLine(),
        requestedDate);

    return existentCreditLineResponse;
  }

  /**
   * Set up the credit line validation logic based on the Strategy design pattern. Select the
   * strategy based on the founding type
   *
   * @param foundingType strategy selector
   */
  private void setFoundingTypeStrategy(FoundingType foundingType) {

    if (SME.equals(foundingType)) {
      foundingTypeStrategy =
          SmeRequesterStrategy.builder().monthlyRevenueRatio(monthlyRevenueRatio).build();
    }

    if (STARTUP.equals(foundingType)) {
      foundingTypeStrategy =
          StartUpRequesterStrategy.builder()
              .cashBalanceRatio(cashBalanceRatio)
              .monthlyRevenueRatio(monthlyRevenueRatio)
              .build();
    }
  }

  private void saveOrUpdateCreditLineResponse(
      CreditLineStatusResponse creditLineStatusResponse,
      BigDecimal approvedCredit,
      ZonedDateTime requestedDate) {

    Integer currentAttempts = creditLineStatusResponse.getAttempts();

    var requestStatus = getCreditLineStatus(approvedCredit);

    creditLineStatusResponse.setAcceptedCreditLine(approvedCredit);
    creditLineStatusResponse.setCreditLineStatus(requestStatus);
    creditLineStatusResponse.setRequestedDate(requestedDate);
    creditLineStatusResponse.setAttempts(++currentAttempts);

    creditLineRequestsRepository.save(
        mapper.mapToCreditLineRequestEntity(creditLineStatusResponse));
  }

  private CreditLineStatus getCreditLineStatus(BigDecimal approvedCredit) {
    return approvedCredit.equals(BigDecimal.ZERO) ? REJECTED : CreditLineStatus.ACCEPTED;
  }
}
