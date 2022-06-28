package org.roy.trb.tst.credit.line.services.strategies;

import static lombok.AccessLevel.PRIVATE;
import static org.roy.trb.tst.credit.line.utils.MathUtils.roundFloatTwoPlaces;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.roy.trb.tst.credit.line.models.RequesterFinancialData;

@Builder
@AllArgsConstructor(access = PRIVATE)
public class StartUpCreditLineValidator implements ICreditLineStrategy {

  private final Integer monthlyRevenueRatio;
  private final Integer cashBalanceRatio;

  @Override
  public Optional<BigDecimal> getCreditLine(RequesterFinancialData financialData) {

    float monthlyRevenueRecommendedCreditLine =
        financialData.getMonthlyRevenue() / monthlyRevenueRatio;

    float cashBalanceRecommendedCreditLine = financialData.getCashBalance() / cashBalanceRatio;

    float overAllRecommendedCreditLine =
        Math.max(monthlyRevenueRecommendedCreditLine, cashBalanceRecommendedCreditLine);

    boolean wasCreditRequestAccepted =
        financialData.getRequestedCredit() <= overAllRecommendedCreditLine;

    BigDecimal acceptedCreditLine = roundFloatTwoPlaces(financialData.getRequestedCredit());

    return wasCreditRequestAccepted ? Optional.of(acceptedCreditLine) : Optional.empty();
  }
}
