package org.roy.credit.line.services.strategies.founding.type;

import static org.roy.credit.line.constants.BusinessRulesConstants.CASH_BALANCE_RATIO;
import static org.roy.credit.line.constants.BusinessRulesConstants.MONTHLY_REVENUE_RATIO;

import java.math.BigDecimal;

import org.roy.credit.line.enums.FoundingType;
import org.roy.credit.line.exceptions.InternalServerErrorException;
import org.roy.credit.line.models.dtos.RequesterFinancialData;

public interface CreditLineCalculationStrategy {

  /**
   * Set up the credit line validation logic based on the Strategy design pattern. Select the
   * strategy based on the founding type
   *
   * @param foundingType strategy selector
   */
  static CreditLineCalculationStrategy getCreditLineCalculationStrategy(FoundingType foundingType) {

    if (FoundingType.SME.equals(foundingType)) {

      return SmeRequesterStrategy.builder().monthlyRevenueRatio(MONTHLY_REVENUE_RATIO).build();
    }

    if (FoundingType.STARTUP.equals(foundingType)) {

      return StartUpRequesterStrategy.builder()
          .cashBalanceRatio(CASH_BALANCE_RATIO)
          .monthlyRevenueRatio(MONTHLY_REVENUE_RATIO)
          .build();
    }

    throw new InternalServerErrorException("Unknown founding type");
  }

  /**
   * Validate a credit line request based on the requester financial data.
   *
   * @param financialData requester financial information
   * @return Optional of the approved credit line request
   */
  BigDecimal getCreditLine(RequesterFinancialData financialData);
}
