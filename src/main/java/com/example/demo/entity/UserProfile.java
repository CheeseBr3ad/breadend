package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String phone;

    // This field references Supabase's `auth.users.id` (UUID)
    @Column(name = "supabase_user_id", nullable = false)
    private UUID supabaseUserId;
}
