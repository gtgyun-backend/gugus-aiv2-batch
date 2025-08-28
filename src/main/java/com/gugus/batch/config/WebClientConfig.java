package com.gugus.batch.config;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();

    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000); // 10초

    @Bean
    public WebClient webClient() {
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return WebClient.builder()
                .uriBuilderFactory(factory)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)                    // connection pool의 갯수
                .pendingAcquireTimeout(Duration.ofSeconds(45)) //커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
//                .pendingAcquireMaxCount(-1)             //커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
//                .maxIdleTime(Duration.ofSeconds(30))  // 유휴 연결 유지 시간 30초
//                .maxLifeTime(Duration.ofMinutes(5))  // 연결의 최대 수명 5분
                .evictInBackground(Duration.ofSeconds(60))  // 60초마다 연결 정리
                .build();
    }


}
