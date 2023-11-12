package com.jeongyuneo.blogsearchservice.global.exception;

public class InvalidArgumentException extends ApplicationException {

    public InvalidArgumentException(ApplicationExceptionInfo exceptionInfo) {
        super(exceptionInfo);
    }
}
