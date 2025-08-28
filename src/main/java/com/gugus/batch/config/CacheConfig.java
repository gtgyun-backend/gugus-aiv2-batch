package com.gugus.batch.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    public CaffeineCacheManager sessionCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("sessionCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .maximumSize(100));
        return cacheManager;
    }

    @Bean
    public CaffeineCacheManager refreshCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("refreshCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
//                .expireAfterWrite(SESSION.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .maximumSize(100)); // 만료 시간 없이 최대 크기만 지정
        return cacheManager;
    }
}