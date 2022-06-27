package org.roy.trb.tst.credit.line.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roy.trb.tst.credit.line.models.RequesterFinancialData;
import org.roy.trb.tst.credit.line.services.strategies.ICreditLineStrategy;
import org.roy.trb.tst.credit.line.services.strategies.SMECreditLineValidator;

@ExtendWith(MockitoExtension.class)
class SMECreditLineValidatorTest {

  private static final Integer MONTHLY_REVENUE_RATIO = 5;

  private static final Float MOCKED_SME_MONTHLY_REVENUE = 100_000.00F;

  private static final Float SME_ACCEPTABLE_CREDIT_LINE =
      (MOCKED_SME_MONTHLY_REVENUE / MONTHLY_REVENUE_RATIO) - 100F;

  private static final Float SME_NON_REJECTABLE_CREDIT_LINE =
      (MOCKED_SME_MONTHLY_REVENUE / MONTHLY_REVENUE_RATIO) + 100F;

  private static ICreditLineStrategy mockedSmeCreditLineStrategy;

  @BeforeAll
  static void setUpMocks() {
    mockedSmeCreditLineStrategy =
        SMECreditLineValidator.builder().monthlyRevenueRatio(MONTHLY_REVENUE_RATIO).build();
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
    BigDecimal expectedAcceptedCreditLine =
        BigDecimal.valueOf(SME_ACCEPTABLE_CREDIT_LINE).setScale(2, RoundingMode.HALF_UP);

    RequesterFinancialData requesterFinancialData =
        RequesterFinancialData.builder()
            .requestedCredit(SME_ACCEPTABLE_CREDIT_LINE)
            .monthlyRevenue(MOCKED_SME_MONTHLY_REVENUE)
            .build();

    // act
    Optional<BigDecimal> rejectedCreditLineRequest =
        mockedSmeCreditLineStrategy.getCreditLine(requesterFinancialData);

    // expect
    assertTrue(rejectedCreditLineRequest.isPresent());
    assertEquals(expectedAcceptedCreditLine, rejectedCreditLineRequest.get());
  }
}
