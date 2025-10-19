package com.example.demo.controller;


import com.example.demo.entity.UserProfile;
import com.example.demo.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;

    public UserProfileController(UserProfileService userProfileService, PasswordEncoder passwordEncoder) {
        this.userProfileService = userProfileService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public UserProfile createProfile(@RequestBody UserProfile profile) {
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return userProfileService.createProfile(profile);
    }


    @GetMapping("/{supabaseUserId}")
    public UserProfile getProfile(@PathVariable UUID supabaseUserId) {
        return userProfileService.getProfileBySupabaseUserId(supabaseUserId);
    }
}
