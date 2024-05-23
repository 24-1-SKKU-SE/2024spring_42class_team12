package com.skku.fixskku.token.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class TokenService {
    // 고정키 사용하여 유효성 판독
    private static final String BASE64_ENCODED_SECRET_KEY = "dO_7N_0ILXdVOGP0sVO6YwD4BNcCQ2uSRmHrYalwk_M=";
    private static final SecretKey key = new SecretKeySpec(Base64.getUrlDecoder().decode(BASE64_ENCODED_SECRET_KEY), SignatureAlgorithm.HS256.getJcaName());

    public String createInfiniteToken() {
        return Jwts.builder()
                .setSubject("User")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ResponseEntity<?> processToken(String token) {
        if (token != null && validateToken(token)) {
            return ResponseEntity.ok(Collections.singletonMap("message", "토큰 확인 성공"));
        } else {
            String newToken = createInfiniteToken();
            return ResponseEntity.ok(Collections.singletonMap("token", newToken));
        }
    }
}
