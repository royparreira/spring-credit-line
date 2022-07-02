package org.roy.trb.tst.credit.line.services.strategies.credit.status;

import org.roy.trb.tst.credit.line.models.daos.CreditLineRequestRecordDao;
import org.roy.trb.tst.credit.line.models.dtos.RequesterFinancialData;
import org.roy.trb.tst.credit.line.services.strategies.founding.type.CreditLineCalculationStrategy;

public class PreviouslyAcceptedStrategy implements CreditRequestStrategy {

  @Override
  public CreditLineRequestRecordDao processCreditLineRequest(
      CreditLineCalculationStrategy creditLineCalculationStrategy,
      RequesterFinancialData requesterFinancialData,
      CreditLineRequestRecordDao creditLineRequestRecordDao) {

    Integer currentAttempts = creditLineRequestRecordDao.getAttempts();

    creditLineRequestRecordDao.setRequestedDate(requesterFinancialData.getRequestedDate());
    creditLineRequestRecordDao.setAttempts(currentAttempts + 1);

    return creditLineRequestRecordDao;
  }
}
