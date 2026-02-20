package com.fytzi.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        System.out.println(
                "JwtUtil (Auth Service) - Injected secret length: " + (secret != null ? secret.length() : "null"));
        if (secret != null && secret.length() >= 4) {
            System.out.println("JwtUtil (Auth Service) - Secret starts with: " + secret.substring(0, 4));
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

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
