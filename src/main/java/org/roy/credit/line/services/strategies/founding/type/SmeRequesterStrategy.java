package org.roy.credit.line.services.strategies.founding.type;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.roy.credit.line.models.dtos.RequesterFinancialData;
import org.roy.credit.line.utils.MathUtils;

@Builder
@AllArgsConstructor(access = PRIVATE)
public class SmeRequesterStrategy implements CreditLineCalculationStrategy {

  private final Integer monthlyRevenueRatio;

  @Override
  public BigDecimal getCreditLine(RequesterFinancialData financialData) {

    float recommendedCreditLine = (financialData.getMonthlyRevenue() / monthlyRevenueRatio);

    boolean wasCreditRequestAccepted =
        financialData.getRequestedCreditLine() <= recommendedCreditLine;

    BigDecimal acceptedCreditLine = MathUtils.roundFloatTwoPlaces(financialData.getRequestedCreditLine());

    return wasCreditRequestAccepted ? acceptedCreditLine : BigDecimal.ZERO;
  }
}
