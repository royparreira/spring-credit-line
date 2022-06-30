package org.roy.trb.tst.credit.line.services;

import java.util.UUID;
import org.roy.trb.tst.credit.line.enums.CreditLineStatus;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.roy.trb.tst.credit.line.models.requests.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.responses.CreditLineApiResponse;

public interface CreditLineService {

  /**
   * Process a credit line request based on the founding type, cash balance and monthly revenue.
   *
   * @param creditLineRequest DTO containing the request data
   * @return credit line request status. Either accepted or rejected
   */
  CreditLineApiResponse validateCreditLine(
      UUID customerId, CreditLineRequest creditLineRequest, FoundingType foundingType);

  CreditLineStatus getCustomerCreditLineStatus(UUID customerId);
}
