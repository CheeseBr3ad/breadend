package com.example.demo.repository;


import com.example.demo.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findBySupabaseUserId(UUID supabaseUserId);
    UserProfile findByUsername(String username);

    UserProfile findByEmail(String email);
}
