package com.jeongyuneo.blogsearchservice.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationExceptionInfo {

    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "WR-001", "잘못된 요청 파라미터가 포함되어 있습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "IS-001", "서버 내부 에러가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
