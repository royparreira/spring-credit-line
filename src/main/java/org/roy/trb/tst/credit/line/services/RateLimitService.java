package org.roy.trb.tst.credit.line.services;

import java.util.UUID;

public interface RateLimitService {

  void setRateLimitForRejectedCredit(UUID customerId);

  void setRateLimitForAcceptedCredit(UUID customerId);

  void checkRateLimitFor(UUID customerId);
}
