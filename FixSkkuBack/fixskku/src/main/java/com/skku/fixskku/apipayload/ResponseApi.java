package com.skku.fixskku.apipayload;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

public class ResponseApi {
    @Getter
    @Builder
    private static class Body {
        private String message;
    }

    public static ResponseEntity of(ResponseStatus status) {
        Body body = Body.builder()
                .message(status.getMessage())
                .build();
        return new ResponseEntity<>(body, status.getHttpStatus());
    }

    // 200
    public static ResponseEntity ok() {
        return of(ResponseStatus._OK);
    }

    // 200 토큰
    public static ResponseEntity tokenOk() {
        return of(ResponseStatus._TOKEN_OK);
    }

    // 400
    public static ResponseEntity badRequest() {
        return of(ResponseStatus._BAD_REQUEST);
    }



    // 401
    public static  ResponseEntity unauthorized() {
        return of(ResponseStatus._UNAUTHORIZED);
    }

    // 404
    public static ResponseEntity notfound() {
        return of(ResponseStatus._NOT_FOUND);
    }

    // 500
    public static ResponseEntity serverError() {
        return of(ResponseStatus._INTERNAL_SERVER_ERROR);
    }
}
