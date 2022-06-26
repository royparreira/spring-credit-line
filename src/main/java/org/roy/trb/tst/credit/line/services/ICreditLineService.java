package org.roy.trb.tst.credit.line.services;

import org.roy.trb.tst.credit.line.models.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.CreditLineResponse;

public interface ICreditLineService {

  /**
   * Process a credit line request based on the founding type, cash balance and monthly revenue.
   *
   * @param creditLineRequest DTO containing the request data
   * @return credit line request status. Either accepted or rejected
   */
  CreditLineResponse validateCreditLine(CreditLineRequest creditLineRequest);
}
