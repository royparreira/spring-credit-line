package org.roy.trb.tst.credit.line.services;

import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.grid.GridBucketState;
import io.github.bucket4j.grid.ProxyManager;
import io.github.bucket4j.grid.jcache.JCache;
import java.util.UUID;
import java.util.function.Supplier;
import javax.cache.Cache;
import org.roy.trb.tst.credit.line.exceptions.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterServiceImpl implements RateLimiterService {

  private final ProxyManager<String> creditLineBuckets;
  @Autowired private CreditLineService creditLineService;

  public RateLimiterServiceImpl(Cache<String, GridBucketState> cache) {
    this.creditLineBuckets = Bucket4j.extension(JCache.class).proxyManagerForCache(cache);
  }

  @Override
  public void checkRateLimit(UUID customerId) throws TooManyRequestsException {

    var creditLineStatus = creditLineService.getCustomerCreditLineStatus(customerId);

    Supplier<BucketConfiguration> configSupplier =
        () ->
            Bucket4j.configurationBuilder()
                .addLimit(creditLineStatus.getLimit())
                .buildConfiguration();

    boolean customerHasQuota =
        creditLineBuckets
            .getProxy(String.format("%s-%s", customerId, creditLineStatus.name()), configSupplier)
            .tryConsume(1);

    if (!customerHasQuota) {
      throw new TooManyRequestsException();
    }
  }
}
