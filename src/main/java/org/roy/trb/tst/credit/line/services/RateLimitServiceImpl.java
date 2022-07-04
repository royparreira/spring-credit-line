package org.roy.trb.tst.credit.line.services;

import static org.roy.trb.tst.credit.line.enums.CreditLineStatus.ACCEPTED;
import static org.roy.trb.tst.credit.line.enums.CreditLineStatus.REJECTED;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.roy.trb.tst.credit.line.enums.CreditLineStatus;
import org.roy.trb.tst.credit.line.exceptions.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RateLimitServiceImpl implements RateLimitService {
  private final RedisTemplate<String, Integer> redisTemplate;

  @Override
  public void setRateLimitForAcceptedCredit(UUID customerId) {
    redisTemplate.opsForValue().setIfAbsent(keyOf(ACCEPTED, customerId), 1, Duration.ofMinutes(2));
  }

  @Override
  public void setRateLimitForRejectedCredit(UUID customerId) {
    redisTemplate.opsForValue().setIfAbsent(keyOf(REJECTED, customerId), 0, Duration.ofSeconds(30));
  }

  @Override
  public void checkRateLimitFor(UUID customerId) {
    checkRejectedCreditCache(customerId);
    checkAcceptedCreditCache(customerId);
  }

  private void checkRejectedCreditCache(UUID customerId) {
    if (Boolean.TRUE.equals(redisTemplate.hasKey(keyOf(REJECTED, customerId)))) {
      throw new TooManyRequestsException();
    }
  }

  private void checkAcceptedCreditCache(UUID customerId) {
    Integer count = redisTemplate.opsForValue().get(keyOf(ACCEPTED, customerId));

    if (Objects.nonNull(count)) {

      if (count >= 3) {
        throw new TooManyRequestsException();
      }
      redisTemplate.opsForValue().increment(keyOf(ACCEPTED, customerId));
    }
  }

  private String keyOf(CreditLineStatus status, UUID customerId) {
    return String.format("%s-%s", status.name(), customerId.toString());
  }
}
