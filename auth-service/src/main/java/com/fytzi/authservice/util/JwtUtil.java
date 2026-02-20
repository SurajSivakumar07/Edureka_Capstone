package com.fytzi.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey key = Keys.hmacShaKeyFor(
            "my-super-secret-key-my-super-secret-key".getBytes()
    );

    private final long EXPIRATION = 1000L * 60 * 60 * 24 * 365 * 10;
    public String generateToken(String userId, String role) {
        return Jwts.builder()
                .subject(userId)
                .claims(Map.of("role", role))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }
}
