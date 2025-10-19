package com.example.demo.service;


import com.example.demo.dto.UserDetailsDto;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile createProfile(UserProfile profile) {
        return userProfileRepository.save(profile);
    }

    public UserProfile getProfileBySupabaseUserId(UUID supabaseUserId) {
        return userProfileRepository.findBySupabaseUserId(supabaseUserId);
    }

    public UserDetailsDto getAuthenticatedUserDetails(String principalName) {
        // In a real application, you'd check which field the principalName corresponds to (email or username).
        // For simplicity, we'll try to find by username or email.
        UserProfile user = userProfileRepository.findByUsername(principalName);
        if (user == null) {
            user = userProfileRepository.findByEmail(principalName);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found for principal: " + principalName);
        }

        return mapToDto(user);
    }

    private UserDetailsDto mapToDto(UserProfile user) {
        return UserDetailsDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .languagePreference(user.getLanguagePreference())
                .supabaseUserId(user.getSupabaseUserId())
                .build();
    }


}
