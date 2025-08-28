package com.gugus.batch.system.dto;

import com.gugus.batch.system.exception.BusinessException;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class SystemErrorDto {
    private String errorCode;
    private String message;
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private Map<String, Object> rest;

    public SystemErrorDto(String restMessage) {
        this.errorCode = "000";
        this.message = "시스템 오류";
        this.rest = Map.of("message", restMessage);
    }

    public SystemErrorDto(HttpStatusCode statusCode, String message) {
        this.errorCode = String.valueOf(statusCode.value());
        this.status = HttpStatus.valueOf(statusCode.value());
        this.message = message;
        this.rest = Map.of();
    }

    public SystemErrorDto(BusinessException exception) {
        this.errorCode = exception.getErrorCode();
        this.message = exception.getMessage();
        this.status = exception.getStatus();
        this.rest = exception.getRest();
    }
}
