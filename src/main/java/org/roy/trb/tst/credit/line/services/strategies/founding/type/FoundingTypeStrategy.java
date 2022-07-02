package org.roy.trb.tst.credit.line.services.strategies.founding.type;

import static org.roy.trb.tst.credit.line.configs.BusinessRulesConfig.CASH_BALANCE_RATIO;
import static org.roy.trb.tst.credit.line.configs.BusinessRulesConfig.MONTHLY_REVENUE_RATIO;
import static org.roy.trb.tst.credit.line.enums.FoundingType.SME;
import static org.roy.trb.tst.credit.line.enums.FoundingType.STARTUP;

import java.math.BigDecimal;
import java.util.Optional;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.roy.trb.tst.credit.line.exceptions.InternalServerErrorException;
import org.roy.trb.tst.credit.line.models.dtos.RequesterFinancialData;

public interface FoundingTypeStrategy {

  /**
   * Set up the credit line validation logic based on the Strategy design pattern. Select the
   * strategy based on the founding type
   *
   * @param foundingType strategy selector
   */
  static FoundingTypeStrategy getFoundingTypeStrategy(FoundingType foundingType) {

    if (SME.equals(foundingType)) {

      return SmeRequesterStrategy.builder().monthlyRevenueRatio(MONTHLY_REVENUE_RATIO).build();
    }

    if (STARTUP.equals(foundingType)) {

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
  Optional<BigDecimal> getCreditLine(RequesterFinancialData financialData);
}
