package org.roy.credit.line.services.strategies.credit.status;

import java.math.BigDecimal;

import org.roy.credit.line.enums.CreditLineStatus;
import org.roy.credit.line.models.daos.CreditLineRequestRecordDao;
import org.roy.credit.line.models.dtos.RequesterFinancialData;
import org.roy.credit.line.services.strategies.founding.type.CreditLineCalculationStrategy;

public class NewOrPreviouslyRejectedRequestStrategy implements CreditRequestStrategy {

  @Override
  public CreditLineRequestRecordDao processCreditLineRequest(
      CreditLineCalculationStrategy creditLineCalculationStrategy,
      RequesterFinancialData requesterFinancialData,
      CreditLineRequestRecordDao creditLineRequestRecordDao) {

    var approvedCredit = creditLineCalculationStrategy.getCreditLine(requesterFinancialData);

    CreditLineStatus creditLineStatus =
        BigDecimal.ZERO.equals(approvedCredit) ? CreditLineStatus.REJECTED : CreditLineStatus.ACCEPTED;

    Integer currentAttempts = creditLineRequestRecordDao.getAttempts();

    creditLineRequestRecordDao.setAcceptedCreditLine(approvedCredit);
    creditLineRequestRecordDao.setCreditLineStatus(creditLineStatus);
    creditLineRequestRecordDao.setRequestedDate(requesterFinancialData.getRequestedDate());
    creditLineRequestRecordDao.setAttempts(currentAttempts + 1);

    return creditLineRequestRecordDao;
  }
}
