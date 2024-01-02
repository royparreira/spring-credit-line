package org.roy.credit.line.services;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.roy.credit.line.enums.CreditLineStatus;
import org.roy.credit.line.exceptions.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RateLimitServiceImpl implements RateLimitService {
  private final RedisTemplate<String, Integer> redisTemplate;

  public static String keyOf(CreditLineStatus status, UUID customerId) {
    return String.format("%s-%s", status.name(), customerId.toString());
  }

  @Override
  public void setRateLimitForAcceptedCredit(UUID customerId) {
    redisTemplate.opsForValue().setIfAbsent(keyOf(CreditLineStatus.ACCEPTED, customerId), 1, Duration.ofMinutes(2));
  }

  @Override
  public void setRateLimitForRejectedCredit(UUID customerId) {
    redisTemplate.opsForValue().setIfAbsent(keyOf(CreditLineStatus.REJECTED, customerId), 0, Duration.ofSeconds(30));
  }

  @Override
  public void checkRateLimitFor(UUID customerId) {
    checkRejectedCreditCache(customerId);
    checkAcceptedCreditCache(customerId);
  }

  private void checkRejectedCreditCache(UUID customerId) {
    if (Boolean.TRUE.equals(redisTemplate.hasKey(keyOf(CreditLineStatus.REJECTED, customerId)))) {
      throw new TooManyRequestsException();
    }
  }

  private void checkAcceptedCreditCache(UUID customerId) {
    Integer count = redisTemplate.opsForValue().get(keyOf(CreditLineStatus.ACCEPTED, customerId));

    if (Objects.nonNull(count)) {

      if (count >= 3) {
        throw new TooManyRequestsException();
      }
      redisTemplate.opsForValue().increment(keyOf(CreditLineStatus.ACCEPTED, customerId));
    }
  }
}
