package org.roy.credit.line.services.strategies.credit.status;

import org.roy.credit.line.enums.CreditLineStatus;
import org.roy.credit.line.exceptions.InternalServerErrorException;
import org.roy.credit.line.models.daos.CreditLineRequestRecordDao;
import org.roy.credit.line.models.dtos.RequesterFinancialData;
import org.roy.credit.line.services.strategies.founding.type.CreditLineCalculationStrategy;

public interface CreditRequestStrategy {
  static CreditRequestStrategy getCreditRequestStrategy(CreditLineStatus lastCreditLineStatus) {

    switch (lastCreditLineStatus) {
      case NONE:
      case REJECTED:
        return new NewOrPreviouslyRejectedRequestStrategy();

      case ACCEPTED:
        return new PreviouslyAcceptedStrategy();

      default:
        throw new InternalServerErrorException("Unknown credit line lastCreditLineStatus");
    }
  }

  CreditLineRequestRecordDao processCreditLineRequest(
      CreditLineCalculationStrategy creditLineCalculationStrategy,
      RequesterFinancialData requesterFinancialData,
      CreditLineRequestRecordDao creditLineRequestRecordDao);
}
