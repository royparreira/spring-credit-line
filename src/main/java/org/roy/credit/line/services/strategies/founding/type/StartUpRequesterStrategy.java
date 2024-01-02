package org.roy.credit.line.services.strategies.founding.type;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.roy.credit.line.models.dtos.RequesterFinancialData;
import org.roy.credit.line.utils.MathUtils;

@Builder
@AllArgsConstructor(access = PRIVATE)
public class StartUpRequesterStrategy implements CreditLineCalculationStrategy {

  private final Integer monthlyRevenueRatio;
  private final Integer cashBalanceRatio;

  @Override
  public BigDecimal getCreditLine(RequesterFinancialData financialData) {

    float monthlyRevenueRecommendedCreditLine =
        financialData.getMonthlyRevenue() / monthlyRevenueRatio;

    float cashBalanceRecommendedCreditLine = financialData.getCashBalance() / cashBalanceRatio;

    float overAllRecommendedCreditLine =
        Math.max(monthlyRevenueRecommendedCreditLine, cashBalanceRecommendedCreditLine);

    boolean wasCreditRequestAccepted =
        financialData.getRequestedCreditLine() <= overAllRecommendedCreditLine;

    BigDecimal acceptedCreditLine = MathUtils.roundFloatTwoPlaces(financialData.getRequestedCreditLine());

    return wasCreditRequestAccepted ? acceptedCreditLine : BigDecimal.ZERO;
  }
}
