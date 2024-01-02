package org.roy.credit.line.services;

import java.util.UUID;

import org.roy.credit.line.enums.FoundingType;
import org.roy.credit.line.models.requests.PostRequestCreditLineRequestBody;
import org.roy.credit.line.models.responses.PostRequestCreditLineResponseBody;

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
}
