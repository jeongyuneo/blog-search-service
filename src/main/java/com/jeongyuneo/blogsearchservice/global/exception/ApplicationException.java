package com.jeongyuneo.blogsearchservice.global.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final ApplicationExceptionInfo exceptionInfo;

    public ApplicationException(ApplicationExceptionInfo exceptionInfo) {
        super(exceptionInfo.getMessage());
        this.exceptionInfo = exceptionInfo;
    }

    public HttpStatus getStatus() {
        return exceptionInfo.getStatus();
    }

    public String getCode() {
        return exceptionInfo.getCode();
    }

    public String getMessage() {
        return exceptionInfo.getMessage();
    }
}
