package com.example.demo.service;


import com.example.demo.entity.UserProfile;
import com.example.demo.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
