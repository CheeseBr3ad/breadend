package com.example.demo.security;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;
import java.util.Optional;

public class JwtUtils {
/*
    public static Optional<Jwt> getJwtFromAuthentication(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return Optional.of(jwtAuth.getToken());
        }
        return Optional.empty();
    }

    public static Optional<String> getUserId(Authentication authentication) {
        return getJwtFromAuthentication(authentication)
                .map(jwt -> jwt.getClaimAsString("sub"));
    }

    public static Optional<String> getUserEmail(Authentication authentication) {
        return getJwtFromAuthentication(authentication)
                .map(jwt -> jwt.getClaimAsString("email"));
    }

    public static Optional<Map<String, Object>> getUserMetadata(Authentication authentication) {
        return getJwtFromAuthentication(authentication)
                .map(jwt -> jwt.getClaimAsMap("user_metadata"));
    }

    public static Optional<Map<String, Object>> getAppMetadata(Authentication authentication) {
        return getJwtFromAuthentication(authentication)
                .map(jwt -> jwt.getClaimAsMap("app_metadata"));
    }

    public static boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }

    public static boolean isAdmin(Authentication authentication) {
        return hasRole(authentication, "ADMIN");
    }

    public static boolean isUser(Authentication authentication) {
        return hasRole(authentication, "USER");
    }*/
}
