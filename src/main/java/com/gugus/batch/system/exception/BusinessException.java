package com.gugus.batch.system.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
    private String message;
    private String errorCode;
    private String language = "ko";
    private Map<String, Object> rest = new HashMap<>();
    private Map<String, String> replace = new HashMap<>();
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    
    public BusinessException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public BusinessException(String errorCode, Map<String, String> replace) {
        super(errorCode);
        this.errorCode = errorCode;
        this.replace = replace;
    }

    public BusinessException(String errorCode, HttpStatus httpStatus) {
        super(errorCode);
        this.errorCode = errorCode;
        this.status = httpStatus;
    }

    public BusinessException(String errorCode, String message) {
        super(errorCode);
        this.errorCode = errorCode;
        this.message = message;
    }

    public BusinessException(String errorCode, String message, String language) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.language = language;
    }

    public static final BusinessException EXCEPTION =
            new BusinessException("000", "현재 서비스 최적화 중입니다. 잠시 후에 다시 시도해주세요.");
}
