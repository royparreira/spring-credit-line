package org.roy.trb.tst.credit.line.services.strategies.founding.type;

import java.math.BigDecimal;
import java.util.Optional;
import org.roy.trb.tst.credit.line.models.dtos.RequesterFinancialData;

public interface FoundingTypeStrategy {

  /**
   * Validate a credit line request based on the requester financial data.
   *
   * @param financialData requester financial information
   * @return Optional of the approved credit line request
   */
  Optional<BigDecimal> getCreditLine(RequesterFinancialData financialData);
}
