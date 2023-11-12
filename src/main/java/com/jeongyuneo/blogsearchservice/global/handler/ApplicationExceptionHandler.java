package com.jeongyuneo.blogsearchservice.global.handler;

import com.jeongyuneo.blogsearchservice.global.dto.ErrorResponse;
import com.jeongyuneo.blogsearchservice.global.exception.ApplicationException;
import com.jeongyuneo.blogsearchservice.global.exception.ApplicationExceptionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(ErrorResponse.from(exception));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, ConstraintViolationException.class})
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Exception exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.from(ApplicationExceptionInfo.INVALID_REQUEST_PARAMETER.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleException(RuntimeException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.from(exception));
    }
}
