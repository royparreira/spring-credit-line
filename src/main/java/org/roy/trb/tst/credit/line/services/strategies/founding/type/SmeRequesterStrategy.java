package org.roy.trb.tst.credit.line.services.strategies.founding.type;

import static lombok.AccessLevel.PRIVATE;
import static org.roy.trb.tst.credit.line.utils.MathUtils.roundFloatTwoPlaces;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.roy.trb.tst.credit.line.models.RequesterFinancialData;

@Builder
@AllArgsConstructor(access = PRIVATE)
public class SmeRequesterStrategy implements FoundingTypeStrategy {

  private final Integer monthlyRevenueRatio;

  @Override
  public Optional<BigDecimal> getCreditLine(RequesterFinancialData financialData) {

    float recommendedCreditLine = (financialData.getMonthlyRevenue() / monthlyRevenueRatio);

    boolean wasCreditRequestAccepted = financialData.getRequestedCredit() <= recommendedCreditLine;

    BigDecimal acceptedCreditLine = roundFloatTwoPlaces(financialData.getRequestedCredit());

    return wasCreditRequestAccepted ? Optional.of(acceptedCreditLine) : Optional.empty();
  }
}
