package com.server.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    private final Key key;
    private final long expiry;
    private static final String[] WHITELIST = {
        "/auth/login",
        "/auth/register",
        "/currency"
    };

    public JWTUtil(io.github.cdimascio.dotenv.Dotenv dotenv) {
        this.key = Keys.hmacShaKeyFor(dotenv.get("JWT_SECRET").getBytes());
        this.expiry = Long.parseLong(dotenv.get("JWT_EXPIRY_MINUTES")) * 60 * 1000;
    }

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiry))
            .signWith(key)
            .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
