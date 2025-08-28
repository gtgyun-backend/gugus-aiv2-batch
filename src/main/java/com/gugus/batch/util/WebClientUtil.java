package com.gugus.batch.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gugus.batch.config.WebClientConfig;
import com.gugus.batch.system.exception.BusinessException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientUtil {

    private final WebClientConfig webClientConfig;

    public <T> Mono<T> get(String url, Class<T> responseDtoClass, HttpHeaders ...headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (HttpHeaders header : headers) {
            httpHeaders.addAll(header);
        }
        return webClientConfig.webClient().method(HttpMethod.GET)
                .uri(url)
                .headers(h -> h.addAll(httpHeaders)) // <-- header 세팅하는 방법
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientResponseError)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleClientResponseError)
                .bodyToMono(responseDtoClass);
    }

    public <T, V> Mono<T> post(String url, V requestDto, Class<T> responseDtoClass, HttpHeaders ...headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (HttpHeaders header : headers) {
            httpHeaders.addAll(header);
        }
        return webClientConfig.webClient().method(HttpMethod.POST)
                .uri(url)
                .headers(h -> h.addAll(httpHeaders)) // <-- header 세팅하는 방법
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientResponseError)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleClientResponseError)
                .bodyToMono(responseDtoClass);
    }

    public <T> Mono<T> api(HttpMethod method, String url, Map<String, Object> requestDto, Class<T> responseDtoClass, HttpHeaders ...headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (HttpHeaders header : headers) {
            httpHeaders.addAll(header);
        }
        var client = webClientConfig.webClient().method(method);
        if(HttpMethod.GET.equals(method)) {
            if(requestDto != null) {
                String queryString = requestDto.entrySet()
                        .stream()
                        .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&"));
                url = url + "?" + queryString;
            }
        } else {
            client.bodyValue(requestDto);
        }
        return client
            .uri(url)
            .headers(h -> h.addAll(httpHeaders))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleClientResponseError)
            .onStatus(HttpStatusCode::is5xxServerError, this::handleClientResponseError)
            .bodyToMono(responseDtoClass);
    }

    public <T, V> Mono<T> delete(String url, V requestDto, Class<T> responseDtoClass, HttpHeaders ...headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (HttpHeaders header : headers) {
            httpHeaders.addAll(header);
        }
        return webClientConfig.webClient().method(HttpMethod.DELETE)
                .uri(url)
                .headers(h -> h.addAll(httpHeaders)) // <-- header 세팅하는 방법
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientResponseError)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleClientResponseError)
                .bodyToMono(responseDtoClass)
                .onErrorResume(e -> Mono.empty());
    }


    public Mono<? extends Throwable> handleClientResponseError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(err -> {
                    if (err == null) {
                        return Mono.error(new WebClientResponseException(
                                clientResponse.statusCode().value(),
                                clientResponse.statusCode().toString(),
                                clientResponse.headers().asHttpHeaders(),
                                null,
                                Charset.defaultCharset()
                        ));
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    var exception = jsonErrorToBusinessException(objectMapper, err);
                    if(exception == null) {
                        var rest = jsonStringToMap(objectMapper, err);
                        exception = new BusinessException(BusinessException.EXCEPTION.getErrorCode(), BusinessException.EXCEPTION.getMessage());
                        if(rest.isEmpty()) {
                            exception.setMessage(err);
                        }
                        exception.setRest(rest);
                    }
                    return Mono.error(exception);
                });
    }

    public BusinessException jsonErrorToBusinessException(ObjectMapper objectMapper, String jsonString) {
        if (jsonString == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, BusinessException.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> jsonStringToMap(ObjectMapper objectMapper, String jsonString) {
        try{
            return objectMapper.readValue(jsonString, new TypeReference<>() {});
        }catch (Exception e) {
            return new HashMap<>();
        }
    }
}
