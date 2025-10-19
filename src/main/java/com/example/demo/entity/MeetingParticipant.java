package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "meeting_participants")
public class MeetingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to the meeting
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    // Reference to the user profile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

    // Time the user joined the meeting
    @Column(name = "join_time", nullable = false)
    private LocalDateTime joinTime;

    // Time the user left the meeting (null if currently in the meeting)
    @Column(name = "leave_time")
    private LocalDateTime leaveTime;

    // The user's preferred language for translation in THIS meeting
    private String sessionLanguagePreference;

    // Combined unique constraint to prevent duplicate entries
    @PrePersist
    @PreUpdate
    private void validate() {
        if (meeting == null || user == null) {
            throw new IllegalStateException("Meeting and User must not be null.");
        }
    }
}