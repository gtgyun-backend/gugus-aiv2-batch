package com.gugus.batch.system.exception;

import com.gugus.batch.system.dto.SystemErrorDto;
import com.gugus.batch.system.repository.SystemErrorHandleRepository;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ErrorHandlers extends ResponseEntityExceptionHandler {

    private final SystemErrorHandleRepository systemErrorHandleRepository;

    private final Map<Class<? extends Exception>, Function<Exception, SystemErrorDto>> exceptionHandlers = Map.of(
            MethodArgumentNotValidException.class, this::handleMethodArgumentNotValid,
            BusinessException.class, this::handleBusinessError
    );

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {

        Function<Exception, SystemErrorDto> handler = exceptionHandlers.get(ex.getClass());
        SystemErrorDto errorDto = handler == null ? handleDefaultError(ex, statusCode) :
                handler.apply(ex);

        return new ResponseEntity<>(errorDto, statusCode);
    }

    // 모든 예외를 잡아서 handleExceptionInternal로 라우팅
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleAllExceptions(BusinessException exception,
            ServletWebRequest request) {
        return handleExceptionInternal(exception, null, new HttpHeaders(), exception.getStatus(), request);
    }

    // 추가해야 할 부분: 모든 일반 예외 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllGeneralExceptions(Exception exception,
            ServletWebRequest request) {
        return handleExceptionInternal(exception, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private SystemErrorDto handleMethodArgumentNotValid(Exception ex) {
        MethodArgumentNotValidException methodEx = (MethodArgumentNotValidException) ex;
        FieldError fieldError = methodEx.getBindingResult().getFieldError();
        String message = fieldError == null ? "유효성 검증 실패" : fieldError.getDefaultMessage();
        return new SystemErrorDto(HttpStatus.BAD_REQUEST, message);
    }

    private SystemErrorDto handleBusinessError(Exception ex) {
        BusinessException businessEx = (BusinessException) ex;
        return systemErrorHandleRepository.errorResponse(businessEx);
    }

    private SystemErrorDto handleDefaultError(Exception ex, HttpStatusCode statusCode) {
        String message = ex.getMessage() == null ? "시스템 오류" : ex.getMessage();
        return new SystemErrorDto(statusCode, message);
    }
}