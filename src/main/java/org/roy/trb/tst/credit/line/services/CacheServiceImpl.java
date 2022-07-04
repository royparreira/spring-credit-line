package org.roy.trb.tst.credit.line.services;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.roy.trb.tst.credit.line.entities.RejectedCreditCache;
import org.roy.trb.tst.credit.line.exceptions.TooManyRequestsException;
import org.roy.trb.tst.credit.line.repositories.RejectedCreditCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CacheServiceImpl implements CacheService {

  private final RejectedCreditCacheRepository rejectedCreditCacheRepository;

  public void checkRejectedCreditCache(UUID customerId) {
    rejectedCreditCacheRepository
        .findById(String.format("REJECTED-%s", customerId))
        .ifPresent(
            s -> {
              throw new TooManyRequestsException();
            });
  }

  public void cacheTheRejectedCredit(UUID customerId) {
    rejectedCreditCacheRepository.save(
        new RejectedCreditCache(String.format("REJECTED-%s", customerId)));
  }
}
