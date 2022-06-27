package org.roy.trb.tst.credit.line.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.CASH_BALANCE_RATIO;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MONTHLY_REVENUE_RATIO;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.mockSmeAcceptableRequest;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.mockSmeRejectableRequest;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.mockStartURejectableRequest;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.mockStartUpAcceptableRequest;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roy.trb.tst.credit.line.enums.CreditLineStatus;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.roy.trb.tst.credit.line.exceptions.RejectedCreditLineException;
import org.roy.trb.tst.credit.line.models.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.CreditLineResponse;
import org.roy.trb.tst.credit.line.services.mappers.CreditLineRequestMapper;
import org.roy.trb.tst.credit.line.utils.MathUtils;

@ExtendWith(MockitoExtension.class)
class CreditLineServiceTest {

  @InjectMocks private static CreditLineServiceImpl creditLineService;
  @Spy private CreditLineRequestMapper mapper = Mappers.getMapper(CreditLineRequestMapper.class);

  private static Stream<Arguments> getAcceptableCreditLineRequests() {
    return Stream.of(
        Arguments.of(mockSmeAcceptableRequest(), FoundingType.SME),
        Arguments.of(mockStartUpAcceptableRequest(), FoundingType.STARTUP));
  }

  private static Stream<Arguments> getRejectableCreditLineRequests() {
    return Stream.of(
        Arguments.of(mockSmeRejectableRequest(), FoundingType.SME),
        Arguments.of(mockStartURejectableRequest(), FoundingType.STARTUP));
  }

  @BeforeEach
  void mockDependencyInjection() {
    creditLineService.setCashBalanceRatio(CASH_BALANCE_RATIO);
    creditLineService.setMonthlyRevenueRatio(MONTHLY_REVENUE_RATIO);
  }

  @ParameterizedTest
  @MethodSource("getAcceptableCreditLineRequests")
  void shouldAcceptCreditLineRequest(
      CreditLineRequest creditLineRequest, FoundingType foundingType) {

    // given
    BigDecimal expectedAcceptedCreditLine =
        MathUtils.roundFloatTwoPlaces(creditLineRequest.getRequestedCreditLine());

    // act
    CreditLineResponse acceptedCreditLine =
        creditLineService.validateCreditLine(
            MOCKED_UUID_CUSTOMER_ID, creditLineRequest, foundingType);

    // expect
    assertEquals(expectedAcceptedCreditLine, acceptedCreditLine.getApprovedCredit());
    assertEquals(CreditLineStatus.ACCEPTED, acceptedCreditLine.getCreditLineStatus());
  }

  @ParameterizedTest
  @MethodSource("getRejectableCreditLineRequests")
  void shouldRejectCreditLineRequest(
      CreditLineRequest creditLineRequest, FoundingType foundingType) {

    assertThrows(
        RejectedCreditLineException.class,
        () -> {
          creditLineService.validateCreditLine(
              MOCKED_UUID_CUSTOMER_ID, creditLineRequest, foundingType);
        });
  }
}
