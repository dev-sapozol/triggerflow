package com.spl.triggerflow.service;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.spl.triggerflow.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        String key = System.getenv("JWT_SECRET_KEY");
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateUserToken(UserEntity user) {
        Instant now = Instant.now();
        Instant expiration = user.getRole().equalsIgnoreCase("SUPERADMIN")
                ? now.plusSeconds(31_536_000) // 1 año
                : now.plusSeconds(86_400); // 1 día

        return Jwts.builder()
                .subject(String.valueOf(user.getId())) 
                .claim("role", user.getRole())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Date getExpirationDate(String token) {
        return extractClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        try {
            return getExpirationDate(token).before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
}