package org.roy.trb.tst.credit.line.services;

import java.util.UUID;
import org.roy.trb.tst.credit.line.exceptions.TooManyRequestsException;

public interface RateLimiterService {
  void checkRateLimit(UUID costumerId) throws TooManyRequestsException;
}
