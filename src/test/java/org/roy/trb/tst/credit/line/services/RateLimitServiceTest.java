package org.roy.trb.tst.credit.line.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.roy.trb.tst.credit.line.enums.CreditLineStatus.ACCEPTED;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID;
import static org.roy.trb.tst.credit.line.services.RateLimitServiceImpl.keyOf;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roy.trb.tst.credit.line.enums.CreditLineStatus;
import org.roy.trb.tst.credit.line.exceptions.TooManyRequestsException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class RateLimitServiceTest {

  @InjectMocks private RateLimitServiceImpl rateLimitService;
  @Mock private RedisTemplate<String, Integer> redisTemplate;
  @Mock private ValueOperations<String, Integer> valueOperations;

  @Test
  void shouldSuccessfullySetRateLimitForAcceptedCredit() {

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(redisTemplate
            .opsForValue()
            .setIfAbsent(
                keyOf(CreditLineStatus.ACCEPTED, MOCKED_UUID_CUSTOMER_ID),
                1,
                Duration.ofMinutes(2)))
        .thenReturn(Boolean.TRUE);

    assertDoesNotThrow(
        () -> rateLimitService.setRateLimitForAcceptedCredit(MOCKED_UUID_CUSTOMER_ID));
  }

  @Test
  void shouldSuccessfullySetRateLimitForRejectedCredit() {

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(redisTemplate
            .opsForValue()
            .setIfAbsent(
                keyOf(CreditLineStatus.REJECTED, MOCKED_UUID_CUSTOMER_ID),
                0,
                Duration.ofSeconds(30)))
        .thenReturn(Boolean.TRUE);

    assertDoesNotThrow(
        () -> rateLimitService.setRateLimitForRejectedCredit(MOCKED_UUID_CUSTOMER_ID));
  }

  @Test
  void shouldThrowTooManyRequestsExceptionWhenHitApiAfterRejectedLessThan30Seconds() {
    when(redisTemplate.hasKey(any())).thenReturn(Boolean.TRUE);
    assertThrows(
        TooManyRequestsException.class,
        () -> rateLimitService.checkRateLimitFor(MOCKED_UUID_CUSTOMER_ID));
  }

  @Test
  void shouldNotThrowTooManyRequestsExceptionWhenHitApiAfterRejectedMoreThan30Seconds() {
    when(redisTemplate.hasKey(any())).thenReturn(Boolean.FALSE);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(redisTemplate.opsForValue().get(keyOf(ACCEPTED, MOCKED_UUID_CUSTOMER_ID)))
        .thenReturn(null);

    assertDoesNotThrow(() -> rateLimitService.checkRateLimitFor(MOCKED_UUID_CUSTOMER_ID));
  }

  @Test
  void shouldThrowTooManyRequestsExceptionWhenHitApiAfterAcceptedMoreThan3timesIn2Minutes() {
    when(redisTemplate.hasKey(any())).thenReturn(Boolean.FALSE);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(redisTemplate.opsForValue().get(keyOf(ACCEPTED, MOCKED_UUID_CUSTOMER_ID))).thenReturn(3);

    assertThrows(
        TooManyRequestsException.class,
        () -> rateLimitService.checkRateLimitFor(MOCKED_UUID_CUSTOMER_ID));
  }

  @Test
  void shouldNotThrowTooManyRequestsExceptionWhenHitApiAfterAcceptedLessThan3timesIn2Minutes() {
    when(redisTemplate.hasKey(any())).thenReturn(Boolean.FALSE);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(redisTemplate.opsForValue().get(keyOf(ACCEPTED, MOCKED_UUID_CUSTOMER_ID))).thenReturn(1);

    assertDoesNotThrow(() -> rateLimitService.checkRateLimitFor(MOCKED_UUID_CUSTOMER_ID));
  }
}
