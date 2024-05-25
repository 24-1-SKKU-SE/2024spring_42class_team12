package com.skku.fixskku.apipayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    _OK(OK, "요청에 성공했습니다."),
    // 가장 일반적인 응답
    _BAD_REQUEST(BAD_REQUEST, "유효하지 않은 데이터 입니다."),
    _UNAUTHORIZED(UNAUTHORIZED, "인증이 필요합니다."),
    _NOT_FOUND(NOT_FOUND, "유효하지 않은 데이터 입니다."),
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    //jwt 토큰 관련
    _TOKEN_OK(OK, "토큰 확인 성공"),

    //관리자 관련
    _ADMIN_NOT_FOUND(NOT_FOUND, "해당 신고가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
