package com.fytzi.apigateway.filter;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {

    private final SecretKey key = Keys.hmacShaKeyFor(
            "my-super-secret-key-my-super-secret-key".getBytes()
    );

    public Claims extractClaims(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        System.out.println("JWT Claims = " + claims);
        return claims;
    }

    public String extractUserId(String token) {
        return  extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) extractClaims(token).get("role");
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}