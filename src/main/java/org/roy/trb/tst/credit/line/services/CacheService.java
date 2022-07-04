package org.roy.trb.tst.credit.line.services;

import java.util.UUID;

public interface CacheService {

  void checkRejectedCreditCache(UUID customerId);

  void cacheTheRejectedCredit(UUID customerId);
}
