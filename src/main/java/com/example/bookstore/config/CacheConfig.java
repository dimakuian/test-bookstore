package com.example.bookstore.config;

import com.example.bookstore.model.CurrencyResponse;
import com.example.bookstore.utils.CacheEventLogger;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.EventType;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager getCacheManager() {

        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        CacheConfigurationBuilder<Long, CurrencyResponse> configurationBuilder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Long.class, CurrencyResponse.class,
                                ResourcePoolsBuilder.heap(1000)
                                        .offheap(25, MemoryUnit.MB))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(30)));

        CacheEventListenerConfigurationBuilder asynchronousListener = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(new CacheEventLogger()
                        , EventType.CREATED, EventType.EXPIRED).unordered().asynchronous();

        cacheManager.createCache("productCatalogConfig",
                Eh107Configuration.fromEhcacheCacheConfiguration(configurationBuilder.withService(asynchronousListener)));

        return cacheManager;
    }
}
