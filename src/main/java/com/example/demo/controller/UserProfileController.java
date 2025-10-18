package com.example.demo.controller;


import com.example.demo.entity.UserProfile;
import com.example.demo.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping
    public UserProfile createProfile(@RequestBody UserProfile profile) {
        return userProfileService.createProfile(profile);
    }

    @GetMapping("/{supabaseUserId}")
    public UserProfile getProfile(@PathVariable UUID supabaseUserId) {
        return userProfileService.getProfileBySupabaseUserId(supabaseUserId);
    }
}
