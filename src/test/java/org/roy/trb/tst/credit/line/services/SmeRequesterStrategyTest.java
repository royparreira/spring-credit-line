package org.roy.trb.tst.credit.line.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_SME_MONTHLY_REVENUE;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MONTHLY_REVENUE_RATIO;
import static org.roy.trb.tst.credit.line.utils.MathUtils.roundFloatTwoPlaces;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roy.trb.tst.credit.line.models.dtos.RequesterFinancialData;
import org.roy.trb.tst.credit.line.services.strategies.founding.type.FoundingTypeStrategy;
import org.roy.trb.tst.credit.line.services.strategies.founding.type.SmeRequesterStrategy;

@ExtendWith(MockitoExtension.class)
class SmeRequesterStrategyTest {

  private static final Float SME_ACCEPTABLE_CREDIT_LINE =
      (MOCKED_SME_MONTHLY_REVENUE / MONTHLY_REVENUE_RATIO) - 100F;

  private static final Float SME_NON_REJECTABLE_CREDIT_LINE =
      (MOCKED_SME_MONTHLY_REVENUE / MONTHLY_REVENUE_RATIO) + 100F;

  private static FoundingTypeStrategy mockedSmeCreditLineStrategy;

  @BeforeAll
  static void setUpMocks() {
    mockedSmeCreditLineStrategy =
        SmeRequesterStrategy.builder().monthlyRevenueRatio(MONTHLY_REVENUE_RATIO).build();
  }

  @Test
  void shouldRejectSMECreditRequestBasedOnMonthlyRevenue() {

    // given
    RequesterFinancialData requesterFinancialData =
        RequesterFinancialData.builder()
            .requestedCredit(SME_NON_REJECTABLE_CREDIT_LINE)
            .monthlyRevenue(MOCKED_SME_MONTHLY_REVENUE)
            .build();

    // act
    Optional<BigDecimal> rejectedCreditLineRequest =
        mockedSmeCreditLineStrategy.getCreditLine(requesterFinancialData);

    // expect
    assertTrue(rejectedCreditLineRequest.isEmpty());
  }

  @Test
  void shouldAcceptSMECreditRequestBasedOnMonthlyRevenue() {

    // given
    BigDecimal expectedAcceptedCreditLine = roundFloatTwoPlaces(SME_ACCEPTABLE_CREDIT_LINE);

    RequesterFinancialData requesterFinancialData =
        RequesterFinancialData.builder()
            .requestedCredit(SME_ACCEPTABLE_CREDIT_LINE)
            .monthlyRevenue(MOCKED_SME_MONTHLY_REVENUE)
            .build();

    // act
    Optional<BigDecimal> acceptedCreditLineRequest =
        mockedSmeCreditLineStrategy.getCreditLine(requesterFinancialData);

    // expect
    assertTrue(acceptedCreditLineRequest.isPresent());
    assertEquals(expectedAcceptedCreditLine, acceptedCreditLineRequest.get());
  }
}
