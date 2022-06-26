package org.roy.trb.tst.credit.line.services.strategies;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.roy.trb.tst.credit.line.models.RequesterFinancialData;

@Builder
@AllArgsConstructor(access = PRIVATE)
public class SMECreditLineValidator implements ICreditLineStrategy {

  private final Integer monthlyRevenueRatio;

  @Override
  public Optional<BigDecimal> getCreditLine(RequesterFinancialData financialData) {

    float recommendedCreditLine = (financialData.getMonthlyRevenue() / monthlyRevenueRatio);

    boolean wasCreditRequestAccepted = financialData.getRequestedCredit() <= recommendedCreditLine;

    BigDecimal acceptedCreditLine =
        BigDecimal.valueOf(financialData.getRequestedCredit()).setScale(2, RoundingMode.HALF_UP);

    return wasCreditRequestAccepted ? Optional.of(acceptedCreditLine) : Optional.empty();
  }
}
