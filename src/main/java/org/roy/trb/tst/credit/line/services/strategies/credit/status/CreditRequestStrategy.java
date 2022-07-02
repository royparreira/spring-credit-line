package org.roy.trb.tst.credit.line.services.strategies.credit.status;

import org.roy.trb.tst.credit.line.enums.CreditLineStatus;
import org.roy.trb.tst.credit.line.exceptions.InternalServerErrorException;
import org.roy.trb.tst.credit.line.models.daos.CreditLineRequestRecordDao;
import org.roy.trb.tst.credit.line.models.dtos.RequesterFinancialData;
import org.roy.trb.tst.credit.line.services.strategies.founding.type.CreditLineCalculationStrategy;

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
