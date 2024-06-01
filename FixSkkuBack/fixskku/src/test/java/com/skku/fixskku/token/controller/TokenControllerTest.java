package com.skku.fixskku.token.controller;

import com.skku.fixskku.common.apipayload.ResponseApi;
import com.skku.fixskku.common.apipayload.ResponseStatus;
import com.skku.fixskku.token.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback
class TokenControllerTest {

    @Autowired
    TokenController tokenController;

    @Autowired
    TokenService tokenService;

    @Test
    void 토큰발급_통합() {
        ResponseEntity<?> responseEntity = tokenController.getToken(null);
        int value = responseEntity.getStatusCode().value();
        assertEquals(HttpStatus.OK.value(), value);
        assertNotNull(responseEntity.getBody(), "Response body should contain a new token");
    }

    @Test
    void 유효한_토큰_검증_통합() {
        // Arrange
        String token = tokenService.createInfiniteToken();

        // Act
        ResponseEntity<?> responseEntity = tokenController.getToken(token);

        // Assert
        int value = responseEntity.getStatusCode().value();
        assertEquals(HttpStatus.OK.value(), value);
        assertEquals(ResponseStatus._TOKEN_OK.getMessage(),
                ((ResponseApi.Body<?>) responseEntity.getBody()).getMessage(),
                "Response message should be 'Token is OK'");
    }
}
