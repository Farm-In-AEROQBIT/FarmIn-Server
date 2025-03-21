package com.farmin.farminserver.handler;

import com.farmin.farminserver.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import com.farmin.farminserver.common.api.Api;
import com.farmin.farminserver.common.error.ErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestController
@Order(value = Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Api<Object>> exception(
            Exception exception
    ){
        log.info("",exception);
        return ResponseEntity
                .status(500)
                .body(Api.ERROR(ErrorCode.SERVER_ERROR));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Api<Object>> handleApiException(ApiException ex) {
        log.error("API Error: {}", ex.getMessage());
        return ResponseEntity.status(ex.getErrorCodeIfs().getHttpStatusCode())
                .body(Api.ERROR(ex.getErrorCodeIfs(), ex.getErrorDescription()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Api<Object>> handleGlobalException(Exception ex) {
        log.error("서버 내부 오류 발생", ex);
        return ResponseEntity.status(500)
                .body(Api.ERROR(ErrorCode.SERVER_ERROR));
    }
}