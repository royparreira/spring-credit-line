package org.roy.trb.tst.credit.line.configs;

import io.github.bucket4j.grid.GridBucketState;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

  @Bean
  Cache<String, GridBucketState> cache() {
    CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
    MutableConfiguration<String, GridBucketState> config = new MutableConfiguration<>();
    return cacheManager.createCache("buckets", config);
  }
}
