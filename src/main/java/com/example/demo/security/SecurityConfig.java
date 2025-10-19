package com.example.demo.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.*;

import org.springframework.security.web.SecurityFilterChain;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, NoPopupAuthEntryPoint entryPoint) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/profiles","/api/login").permitAll() // Exclude this endpoint
                        .anyRequest().authenticated() // Require auth for everything else
                )
                .httpBasic(basic -> basic.authenticationEntryPoint(entryPoint))
                .csrf()
                .disable(); // Enable HTTP Basic authentication

        return http.build();
    }


    // Password encoder (BCrypt is recommended)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")       // Apply CORS to all endpoints
                        .allowedOriginPatterns("*") // Allow all origins
                        .allowedMethods("*")        // Allow all HTTP methods (GET, POST, etc.)
                        .allowedHeaders("*")        // Allow all headers
                        .allowCredentials(true);   // Allow credentials like cookies
            }
        };
    }


}

