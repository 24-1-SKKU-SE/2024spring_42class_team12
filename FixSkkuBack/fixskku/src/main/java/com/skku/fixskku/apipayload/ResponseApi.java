package com.skku.fixskku.apipayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

public class ResponseApi {
    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Body<T> {
        private int code;
        private String message;
        private T data;

        public Body(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }

    public static ResponseEntity<Body<Void>> of(ResponseStatus status) {
        Body<Void> body = Body.<Void>builder()
                .code(status.getHttpStatus().value())
                .message(status.getMessage())
                .build();
        return new ResponseEntity<>(body, status.getHttpStatus());
    }

    public static <T> ResponseEntity<Body<T>> of(ResponseStatus status, T data) {
        Body<T> body = Body.<T>builder()
                .code(status.getHttpStatus().value())
                .message(status.getMessage())
                .data(data)
                .build();
        return new ResponseEntity<>(body, status.getHttpStatus());
    }

    // 200
    public static ResponseEntity<Body<Void>> ok() {
        return of(ResponseStatus._OK);
    }

    // 200 토큰
    public static ResponseEntity<Body<Void>> tokenOk() {
        return of(ResponseStatus._TOKEN_OK);
    }

    // 400
    public static ResponseEntity<Body<Void>> badRequest() {
        return of(ResponseStatus._BAD_REQUEST);
    }

    // 401
    public static ResponseEntity<Body<Void>> unauthorized() {
        return of(ResponseStatus._UNAUTHORIZED);
    }

    // 404
    public static ResponseEntity<Body<Void>> notFound() {
        return of(ResponseStatus._NOT_FOUND);
    }

    // 500
    public static ResponseEntity<Body<Void>> serverError() {
        return of(ResponseStatus._INTERNAL_SERVER_ERROR);
    }
}
