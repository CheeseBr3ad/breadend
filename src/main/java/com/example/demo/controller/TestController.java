package com.example.demo.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // JWT-authenticated endpoint
    @GetMapping("/api/jwt-user")
    @PreAuthorize("hasAnyRole('authenticated')")
    public String jwtUser() {

        return "JWT user: ";
    }

    // Basic auth fallback endpoint
    @GetMapping("/basic-user")
    public String basicUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        // Access the username from local UserProfile
        return "Basic auth user: " + user.getUsername();
    }
}
