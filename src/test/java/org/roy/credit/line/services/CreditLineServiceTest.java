package org.roy.credit.line.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.roy.credit.line.constants.BusinessRulesConstants.MAX_NUMBER_OF_FAILED_ATTEMPTS;
import static org.roy.credit.line.constants.Messages.SALES_AGENT_MSG;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roy.credit.line.enums.CreditLineStatus;
import org.roy.credit.line.enums.FoundingType;
import org.roy.credit.line.exceptions.RejectedCreditLineException;
import org.roy.credit.line.fixture.CreditLineEntityFixture;
import org.roy.credit.line.fixture.CreditLineRequestFixture;
import org.roy.credit.line.models.requests.PostRequestCreditLineRequestBody;
import org.roy.credit.line.models.responses.PostRequestCreditLineResponseBody;
import org.roy.credit.line.repositories.CreditLineRequestRepository;
import org.roy.credit.line.services.mappers.CreditLineRequestMapper;
import org.roy.credit.line.utils.MathUtils;

@ExtendWith(MockitoExtension.class)
class CreditLineServiceTest {

  @InjectMocks private CreditLineServiceImpl creditLineService;
  @Spy private CreditLineRequestMapper mapper = Mappers.getMapper(CreditLineRequestMapper.class);
  @Mock private CreditLineRequestRepository creditLineRequestsRepository;
  @Mock private RateLimitService rateLimitService;

  private static Stream<Arguments> getAcceptableCreditLineRequests() {

    return Stream.of(
        Arguments.of(CreditLineRequestFixture.mockSmeAcceptableRequest(), FoundingType.SME),
        Arguments.of(CreditLineRequestFixture.mockStartUpAcceptableRequest(), FoundingType.STARTUP));
  }

  private static Stream<Arguments> getRejectableCreditLineRequests() {

    return Stream.of(
        Arguments.of(CreditLineRequestFixture.mockSmeRejectableRequest(), FoundingType.SME),
        Arguments.of(CreditLineRequestFixture.mockStartURejectableRequest(), FoundingType.STARTUP));
  }

  private static Stream<Arguments> getMixCreditLineRequests() {
    return Stream.concat(getAcceptableCreditLineRequests(), getRejectableCreditLineRequests());
  }

  @ParameterizedTest
  @MethodSource("getAcceptableCreditLineRequests")
  void shouldAcceptNewCreditLineRequest(
      PostRequestCreditLineRequestBody postRequestCreditLineRequestBody,
      FoundingType foundingType) {

    // given
    BigDecimal expectedAcceptedCreditLine =
        MathUtils.roundFloatTwoPlaces(postRequestCreditLineRequestBody.getRequestedCreditLine());

    lenient()
        .when(creditLineRequestsRepository.findById(any(UUID.class)))
        .thenReturn(Optional.empty());

    doNothing().when(rateLimitService).setRateLimitForAcceptedCredit(any(UUID.class));

    // act
    PostRequestCreditLineResponseBody acceptedCreditLine =
        creditLineService.requestCreditLine(
            CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID, postRequestCreditLineRequestBody, foundingType);

    // expect
    assertEquals(expectedAcceptedCreditLine, acceptedCreditLine.getAcceptedCreditLine());
    Assertions.assertEquals(CreditLineStatus.ACCEPTED, acceptedCreditLine.getCreditLineStatus());
  }

  @ParameterizedTest
  @MethodSource("getRejectableCreditLineRequests")
  void shouldRejectNewCreditLineRequest(
      PostRequestCreditLineRequestBody postRequestCreditLineRequestBody,
      FoundingType foundingType) {

    // given
    lenient()
        .when(creditLineRequestsRepository.findById(any(UUID.class)))
        .thenReturn(Optional.empty());

    doNothing().when(rateLimitService).setRateLimitForRejectedCredit(any(UUID.class));

    // act and expect
    assertThrows(
        RejectedCreditLineException.class,
        () ->
            creditLineService.requestCreditLine(
                CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID, postRequestCreditLineRequestBody, foundingType));
  }

  // Accept already accepted
  @ParameterizedTest
  @MethodSource("getMixCreditLineRequests")
  void shouldAcceptAlreadyAcceptedCreditLineRequestWithSameValuesRegardlessTheInput(
      PostRequestCreditLineRequestBody postRequestCreditLineRequestBody,
      FoundingType foundingType) {

    // given
    lenient()
        .when(creditLineRequestsRepository.findById(any(UUID.class)))
        .thenReturn(CreditLineEntityFixture.mockAlreadyAcceptedRequest());

    doNothing().when(rateLimitService).setRateLimitForAcceptedCredit(any(UUID.class));

    // act
    PostRequestCreditLineResponseBody postRequestCreditLineResponseBody =
        creditLineService.requestCreditLine(
            CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID, postRequestCreditLineRequestBody, foundingType);

    // expect
    Assertions.assertEquals(CreditLineStatus.ACCEPTED, postRequestCreditLineResponseBody.getCreditLineStatus());
    assertEquals(
        new BigDecimal("10000.00"), postRequestCreditLineResponseBody.getAcceptedCreditLine());
  }

  // Accept already rejected - less than more than maximum allowed
  @ParameterizedTest
  @MethodSource("getAcceptableCreditLineRequests")
  void shouldAcceptAlreadyRejectedCreditLineRequestIfNotRejectedMoreThanMaximumAllowed(
      PostRequestCreditLineRequestBody postRequestCreditLineRequestBody,
      FoundingType foundingType) {

    // given
    lenient()
        .when(creditLineRequestsRepository.findById(any(UUID.class)))
        .thenReturn(CreditLineEntityFixture.mockAlreadyRejectedRequest(MAX_NUMBER_OF_FAILED_ATTEMPTS));

    doNothing().when(rateLimitService).setRateLimitForAcceptedCredit(any(UUID.class));

    // act
    PostRequestCreditLineResponseBody postRequestCreditLineResponseBody =
        creditLineService.requestCreditLine(
            CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID, postRequestCreditLineRequestBody, foundingType);

    // expect
    Assertions.assertEquals(CreditLineStatus.ACCEPTED, postRequestCreditLineResponseBody.getCreditLineStatus());
  }

  // Reject already rejected - more than maximum allowed
  @ParameterizedTest
  @MethodSource("getRejectableCreditLineRequests")
  void shouldRejectAlreadyRejectedCreditLineRequestIfRejectedMoreThanMaximumAllowed(
      PostRequestCreditLineRequestBody postRequestCreditLineRequestBody,
      FoundingType foundingType) {

    // given
    lenient()
        .when(creditLineRequestsRepository.findById(any(UUID.class)))
        .thenReturn(CreditLineEntityFixture.mockAlreadyRejectedRequest(MAX_NUMBER_OF_FAILED_ATTEMPTS));

    // act
    RejectedCreditLineException exception =
        assertThrows(
            RejectedCreditLineException.class,
            () ->
                creditLineService.requestCreditLine(
                    CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID, postRequestCreditLineRequestBody, foundingType));

    // expect
    assertEquals(SALES_AGENT_MSG, exception.getCustomMessage());
  }

  // Reject already rejected less than maximum allowed
  @ParameterizedTest
  @MethodSource("getRejectableCreditLineRequests")
  void shouldRejectAlreadyRejectedCreditLineRequestWithCustomMessageMoreThanThreeFailures(
      PostRequestCreditLineRequestBody postRequestCreditLineRequestBody,
      FoundingType foundingType) {

    // given
    lenient()
        .when(creditLineRequestsRepository.findById(any(UUID.class)))
        .thenReturn(CreditLineEntityFixture.mockAlreadyRejectedRequest(MAX_NUMBER_OF_FAILED_ATTEMPTS - 1));

    doNothing().when(rateLimitService).setRateLimitForRejectedCredit(any(UUID.class));

    // act
    RejectedCreditLineException exception =
        assertThrows(
            RejectedCreditLineException.class,
            () ->
                creditLineService.requestCreditLine(
                    CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID, postRequestCreditLineRequestBody, foundingType));

    // expect
    assertTrue(exception.getCustomMessage().isEmpty());
  }
}
