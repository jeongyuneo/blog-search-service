package com.jeongyuneo.blogsearchservice.global.handler;

import com.jeongyuneo.blogsearchservice.global.dto.ErrorResponse;
import com.jeongyuneo.blogsearchservice.global.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
