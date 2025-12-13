package com.server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    // All paths that do NOT require a JWT token
    private static final String[] WHITELIST = {
        "/auth/login",
        "/auth/register",
        "/currency"
    };

    public JwtAuthFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest req,
        HttpServletResponse res,
        FilterChain chain
    ) throws ServletException, IOException {

        String path = req.getRequestURI();

        // Allow whitelisted endpoints without JWT
        for (String open : WHITELIST) {
            if (path.startsWith(open)) {
                chain.doFilter(req, res);
                return;
            }
        }

        // Read Authorization header
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            res.setStatus(401);
            return;
        }

        String token = header.substring(7);

        // Validate token
        if (!jwtUtil.validate(token)) {
            res.setStatus(401);
            return;
        }

        // Extract username from token
        String username = jwtUtil.extractUsername(token);

        // Put user into Spring Security Context
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(username, null, null);

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Continue request
        chain.doFilter(req, res);
    }
}
