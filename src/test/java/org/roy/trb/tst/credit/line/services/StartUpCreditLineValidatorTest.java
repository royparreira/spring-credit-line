package org.roy.trb.tst.credit.line.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.roy.trb.tst.credit.line.utils.MathUtils.roundFloatTwoPlaces;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roy.trb.tst.credit.line.models.RequesterFinancialData;
import org.roy.trb.tst.credit.line.services.strategies.ICreditLineStrategy;
import org.roy.trb.tst.credit.line.services.strategies.StartUpCreditLineValidator;

@ExtendWith(MockitoExtension.class)
class StartUpCreditLineValidatorTest {

  private static final Integer MONTHLY_REVENUE_RATIO = 5;
  private static final Integer CASH_BALANCE_RATIO = 3;

  private static final Float MOCKED_START_UP_GREATER_MONTHLY_REVENUE = 150_000.00F;
  private static final Float MOCKED_START_UP_SMALLER_MONTHLY_REVENUE = 15_000.00F;
  private static final Float MOCKED_START_UP_GREATER_CASH_BALANCE = 150_000.00F;
  private static final Float MOCKED_START_UP_SMALLER_CASH_BALANCE = 15_000.00F;

  private static ICreditLineStrategy mockedStartUpCreditLineStrategy;

  @BeforeAll
  static void setUpMocks() {

    mockedStartUpCreditLineStrategy =
        StartUpCreditLineValidator.builder()
            .monthlyRevenueRatio(MONTHLY_REVENUE_RATIO)
            .cashBalanceRatio(CASH_BALANCE_RATIO)
            .build();
  }

  private static Stream<Arguments> getRejectStartUpCreditLineTestCases() {
    return Stream.of(
        // Monthly Revenue < Cash Balance - Recommended credit = 50_000F
        Arguments.of(
            51_000F, MOCKED_START_UP_SMALLER_MONTHLY_REVENUE, MOCKED_START_UP_GREATER_CASH_BALANCE),

        // Monthly Revenue = Cash Balance - Recommended credit = 50_000F
        Arguments.of(
            51_000F, MOCKED_START_UP_SMALLER_MONTHLY_REVENUE, MOCKED_START_UP_SMALLER_CASH_BALANCE),

        // Monthly Revenue > Cash Balance Recommended credit = 30_000F
        Arguments.of(
            31_000F,
            MOCKED_START_UP_GREATER_MONTHLY_REVENUE,
            MOCKED_START_UP_SMALLER_CASH_BALANCE));
  }

  private static Stream<Arguments> getAcceptStartUpCreditLineTestCases() {
    return Stream.of(
        // Monthly Revenue < Cash Balance - Recommended credit = 50_000F
        Arguments.of(
            5_000F, MOCKED_START_UP_SMALLER_MONTHLY_REVENUE, MOCKED_START_UP_GREATER_CASH_BALANCE),

        // Monthly Revenue = Cash Balance - Recommended credit = 50_000F
        Arguments.of(
            5_000F, MOCKED_START_UP_SMALLER_MONTHLY_REVENUE, MOCKED_START_UP_SMALLER_CASH_BALANCE),

        // Monthly Revenue > Cash Balance Recommended credit = 30_000F
        Arguments.of(
            5_000F, MOCKED_START_UP_GREATER_MONTHLY_REVENUE, MOCKED_START_UP_SMALLER_CASH_BALANCE));
  }

  @ParameterizedTest
  @MethodSource("getRejectStartUpCreditLineTestCases")
  void shouldRejectStartUpCreditRequestBasedOnMonthlyRevenue(
      Float requestedCredit, Float monthlyRevenue, Float cashBalance) {

    // given
    RequesterFinancialData requesterFinancialData =
        RequesterFinancialData.builder()
            .requestedCredit(requestedCredit)
            .monthlyRevenue(monthlyRevenue)
            .cashBalance(cashBalance)
            .build();

    // act
    Optional<BigDecimal> rejectedCreditLineRequest =
        mockedStartUpCreditLineStrategy.getCreditLine(requesterFinancialData);

    // expect
    assertTrue(rejectedCreditLineRequest.isEmpty());
  }

  @ParameterizedTest
  @MethodSource("getAcceptStartUpCreditLineTestCases")
  void shouldAcceptStartUpCreditRequestBasedOnMonthlyRevenue(
      Float requestedCredit, Float monthlyRevenue, Float cashBalance) {

    // given
    BigDecimal expectedAcceptedCreditLine = roundFloatTwoPlaces(requestedCredit);

    RequesterFinancialData requesterFinancialData =
        RequesterFinancialData.builder()
            .requestedCredit(requestedCredit)
            .monthlyRevenue(monthlyRevenue)
            .cashBalance(cashBalance)
            .build();

    // act
    Optional<BigDecimal> acceptedCreditLineRequest =
        mockedStartUpCreditLineStrategy.getCreditLine(requesterFinancialData);

    // expect
    assertTrue(acceptedCreditLineRequest.isPresent());
    assertEquals(expectedAcceptedCreditLine, acceptedCreditLineRequest.get());
  }
}
