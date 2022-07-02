package org.roy.trb.tst.credit.line.services.strategies.founding.type;

import static lombok.AccessLevel.PRIVATE;
import static org.roy.trb.tst.credit.line.utils.MathUtils.roundFloatTwoPlaces;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.roy.trb.tst.credit.line.models.dtos.RequesterFinancialData;

@Builder
@AllArgsConstructor(access = PRIVATE)
public class SmeRequesterStrategy implements CreditLineCalculationStrategy {

  private final Integer monthlyRevenueRatio;

  @Override
  public BigDecimal getCreditLine(RequesterFinancialData financialData) {

    float recommendedCreditLine = (financialData.getMonthlyRevenue() / monthlyRevenueRatio);

    boolean wasCreditRequestAccepted =
        financialData.getRequestedCreditLine() <= recommendedCreditLine;

    BigDecimal acceptedCreditLine = roundFloatTwoPlaces(financialData.getRequestedCreditLine());

    return wasCreditRequestAccepted ? acceptedCreditLine : BigDecimal.ZERO;
  }
}
