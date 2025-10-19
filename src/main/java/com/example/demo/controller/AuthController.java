package com.example.demo.controller;

import com.example.demo.entity.UserProfile;
import com.example.demo.repository.UserProfileRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing credentials"));
        }

        // 1. Find user by username
        UserProfile user = userProfileRepository.findByEmail(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        // 2. Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        // 3. Generate Base64-encoded Basic Auth token
        String encoded = Base64.getEncoder()
                .encodeToString((user.getEmail() + ":" + request.getPassword())
                        .getBytes(StandardCharsets.UTF_8));
        String token = "Basic " + encoded;

        return ResponseEntity.ok(Map.of("token", token));
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
