package com.skku.fixskku.token.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skku.fixskku.token.service.TokenService;

@RestController
@RequestMapping("/users")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/token")
    public ResponseEntity<?> getToken(@RequestHeader(value = "token", required = false) String token) {
        return tokenService.processToken(token);
    }
}
