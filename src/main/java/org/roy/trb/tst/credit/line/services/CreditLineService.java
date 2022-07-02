package org.roy.trb.tst.credit.line.services;

import java.util.UUID;
import org.roy.trb.tst.credit.line.enums.CreditLineStatus;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.roy.trb.tst.credit.line.models.requests.PostRequestCreditLineRequestBody;
import org.roy.trb.tst.credit.line.models.responses.PostRequestCreditLineResponseBody;

public interface CreditLineService {

  /**
   * Process a credit line request based on the founding type, cash balance and monthly revenue.
   *
   * @param postRequestCreditLineRequestBody DTO containing the request data
   * @return credit line request status. Either accepted or rejected
   */
  PostRequestCreditLineResponseBody requestCreditLine(
      UUID customerId,
      PostRequestCreditLineRequestBody postRequestCreditLineRequestBody,
      FoundingType foundingType);

  /**
   * Get the credit line status for a given costumer.
   *
   * @param customerId id of the customer
   * @return The customer credit line status. If it's a new request it'll be considered as rejected.
   */
  CreditLineStatus getCustomerCreditLineStatus(UUID customerId);
}
