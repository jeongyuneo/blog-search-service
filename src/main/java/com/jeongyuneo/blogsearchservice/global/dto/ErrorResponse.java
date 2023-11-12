package com.jeongyuneo.blogsearchservice.global.dto;

import com.jeongyuneo.blogsearchservice.global.exception.ApplicationException;
import com.jeongyuneo.blogsearchservice.global.exception.ApplicationExceptionInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;

    public static ErrorResponse from(String code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse from(ApplicationExceptionInfo exceptionInfo) {
        return new ErrorResponse(exceptionInfo.getCode(), exceptionInfo.getMessage());
    }

    public static ErrorResponse from(ApplicationException exception) {
        return new ErrorResponse(exception.getCode(), exception.getMessage());
    }

    public static ErrorResponse from(Exception exception) {
        return new ErrorResponse(ApplicationExceptionInfo.INTERNAL_SERVER_ERROR.getCode(), exception.getMessage());
    }
}
