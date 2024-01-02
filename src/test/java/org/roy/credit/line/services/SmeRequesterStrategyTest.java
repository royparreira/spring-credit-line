package org.roy.credit.line.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roy.credit.line.fixture.CreditLineRequestFixture;
import org.roy.credit.line.models.dtos.RequesterFinancialData;
import org.roy.credit.line.utils.MathUtils;
import org.roy.credit.line.services.strategies.founding.type.CreditLineCalculationStrategy;
import org.roy.credit.line.services.strategies.founding.type.SmeRequesterStrategy;

@ExtendWith(MockitoExtension.class)
class SmeRequesterStrategyTest {

  private static final Float SME_ACCEPTABLE_CREDIT_LINE =
      (CreditLineRequestFixture.MOCKED_SME_MONTHLY_REVENUE / CreditLineRequestFixture.MONTHLY_REVENUE_RATIO) - 100F;

  private static final Float SME_NON_REJECTABLE_CREDIT_LINE =
      (CreditLineRequestFixture.MOCKED_SME_MONTHLY_REVENUE / CreditLineRequestFixture.MONTHLY_REVENUE_RATIO) + 100F;

  private static CreditLineCalculationStrategy mockedSmeCreditLineStrategy;

  @BeforeAll
  static void setUpMocks() {
    mockedSmeCreditLineStrategy =
        SmeRequesterStrategy.builder().monthlyRevenueRatio(CreditLineRequestFixture.MONTHLY_REVENUE_RATIO).build();
  }

  @Test
  void shouldRejectSMECreditRequestBasedOnMonthlyRevenue() {

    // given
    RequesterFinancialData requesterFinancialData =
        RequesterFinancialData.builder()
            .requestedCreditLine(SME_NON_REJECTABLE_CREDIT_LINE)
            .monthlyRevenue(CreditLineRequestFixture.MOCKED_SME_MONTHLY_REVENUE)
            .build();

    // act
    BigDecimal rejectedCreditLineRequest =
        mockedSmeCreditLineStrategy.getCreditLine(requesterFinancialData);

    // expect
    assertEquals(BigDecimal.ZERO, rejectedCreditLineRequest);
  }

  @Test
  void shouldAcceptSMECreditRequestBasedOnMonthlyRevenue() {

    // given
    BigDecimal expectedAcceptedCreditLine = MathUtils.roundFloatTwoPlaces(SME_ACCEPTABLE_CREDIT_LINE);

    RequesterFinancialData requesterFinancialData =
        RequesterFinancialData.builder()
            .requestedCreditLine(SME_ACCEPTABLE_CREDIT_LINE)
            .monthlyRevenue(CreditLineRequestFixture.MOCKED_SME_MONTHLY_REVENUE)
            .build();

    // act
    BigDecimal acceptedCreditLineRequest =
        mockedSmeCreditLineStrategy.getCreditLine(requesterFinancialData);

    // expect
    assertEquals(expectedAcceptedCreditLine, acceptedCreditLineRequest);
  }
}
