package com.example.demo.controller;


import com.example.demo.dto.UserDetailsDto;
import com.example.demo.entity.UserProfile;
import com.example.demo.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        profile.setUsername(profile.getEmail());
        return userProfileService.createProfile(profile);
    }


    @GetMapping("/{supabaseUserId}")
    public UserProfile getProfile(@PathVariable UUID supabaseUserId) {
        return userProfileService.getProfileBySupabaseUserId(supabaseUserId);
    }

    /**
     * GET /api/v1/users/me
     * Returns the details of the currently authenticated user.
     *
     * @param principal The object representing the currently logged-in user from Spring Security.
     * @return 200 OK with the UserDetailsDto.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDetailsDto> getAuthenticatedUserDetails(Principal principal) {
        if (principal == null) {
            // This should ideally be caught earlier by Spring Security filter chain (401 Unauthorized)
            // But as a safeguard:
            return ResponseEntity.status(401).build();
        }

        // The principal.getName() usually returns the username or email of the authenticated user
        String principalName = principal.getName();

        UserDetailsDto userDetails = userProfileService.getAuthenticatedUserDetails(principalName);

        return ResponseEntity.ok(userDetails);
    }
}
