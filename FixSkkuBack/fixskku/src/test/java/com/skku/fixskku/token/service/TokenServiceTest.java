package com.skku.fixskku.token.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    void 토큰유효성검사() {
        // Act
        String token = tokenService.createInfiniteToken();

        // Assert
        Assertions.assertNotNull(token, "Token should not be null");
        Assertions.assertTrue(tokenService.validateToken(token), "Generated token should be valid");
    }

    @Test
    void 토큰검증() {
        // Arrange
        String token = tokenService.createInfiniteToken();

        // Act & Assert
        Assertions.assertTrue(tokenService.validateToken(token), "Token validation should succeed for a valid token");
    }

    @Test
    void 토큰발급() {
        // Act
        ResponseEntity<?> response = tokenService.processToken(null);

        // Assert
        Assertions.assertEquals(200, response.getStatusCodeValue(), "Response status should be 200 OK");
        Assertions.assertNotNull(response.getBody(), "Response body should contain a new token");
    }
}