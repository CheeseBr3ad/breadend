package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "meetings")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The 'meeting_uuid' for external use/sharing
    @Column(name = "meeting_uuid", unique = true, nullable = false)
    private UUID meetingUuid;

    // The 'meeting short' description/title
    @Column(name = "short_title", nullable = false)
    private String shortTitle;

    // A more detailed description of the meeting
    @Lob // Use @Lob for potentially long text fields
    private String description;

    // The user who initiated the meeting (Host)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private UserProfile host;

    // Start time of the session
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    // End time of the session (null if ongoing)
    @Column(name = "end_time")
    private LocalDateTime endTime;

    // Flag to indicate if the session is currently live
    private boolean isLive = true;
}