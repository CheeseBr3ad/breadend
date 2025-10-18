package com.example.demo.controller;

import com.example.demo.security.JwtUtils;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/api/public/health")
    public String health() {
        return "Service is healthy!";
    }

    @GetMapping("/api/admin")
    //@PreAuthorize("hasRole('ADMIN')")
    public String admin(//Authentication authentication//
                         ) {
        String userId = "";//JwtUtils.getUserId(authentication).orElse("Unknown");
        String email = "";//JwtUtils.getUserEmail(authentication).orElse("Unknown");
        return String.format("You are an admin! User ID: %s, Email: %s", userId, email);
    }

    /*@GetMapping("/api/user")
    //@PreAuthorize("hasRole('USER')")
    public String user(Authentication authentication) {
        String userId = JwtUtils.getUserId(authentication).orElse("Unknown");
        String email = JwtUtils.getUserEmail(authentication).orElse("Unknown");
        return String.format("Hello user! User ID: %s, Email: %s, Authorities: %s", 
                           userId, email, authentication.getAuthorities());
    }

    @GetMapping("/api/authenticated")
    public String authenticated(Authentication authentication) {
        String userId = JwtUtils.getUserId(authentication).orElse("Unknown");
        String email = JwtUtils.getUserEmail(authentication).orElse("Unknown");
        Map<String, Object> userMetadata = JwtUtils.getUserMetadata(authentication).orElse(Map.of());
        Map<String, Object> appMetadata = JwtUtils.getAppMetadata(authentication).orElse(Map.of());
        
        return String.format("Hello authenticated user! " +
                           "User ID: %s, Email: %s, " +
                           "Authorities: %s, " +
                           "User Metadata: %s, " +
                           "App Metadata: %s",
                           userId, email, authentication.getAuthorities(), 
                           userMetadata, appMetadata);
    }

    @GetMapping("/api/profile")
    public Map<String, Object> profile(//Authentication authentication
                                       // ) {
        return Map.of(
            "userId", JwtUtils.getUserId(authentication).orElse("Unknown"),
            "email", JwtUtils.getUserEmail(authentication).orElse("Unknown"),
            "authorities", authentication.getAuthorities(),
            "userMetadata", JwtUtils.getUserMetadata(authentication).orElse(Map.of()),
            "appMetadata", JwtUtils.getAppMetadata(authentication).orElse(Map.of()),
            "isAdmin", JwtUtils.isAdmin(authentication),
            "isUser", JwtUtils.isUser(authentication)
        );
    }*/
}
