package com.example.demo.service;

// MeetingService.java

import com.example.demo.dto.MeetingDto;
import com.example.demo.dto.MeetingStartRequest;
import com.example.demo.entity.Meeting;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.MeetingRepository;
import com.example.demo.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserProfileRepository userProfileRepository; // Assumed for host lookup

    /**
     * Starts a new communication session (Meeting).
     * @param request Contains the host ID, title, and description.
     * @return The DTO of the newly created meeting.
     */
    @Transactional
    public MeetingDto startMeeting(MeetingStartRequest request) {
        // 1. Validate and fetch the host
        UserProfile host;
        try {
            host = userProfileRepository.findByEmail(request.getHostId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
                //.orElseThrow(() -> new RuntimeException("Host user not foun with ID: " + request.getHostId()));

        // 2. Create and save the new Meeting entity
        Meeting meeting = Meeting.builder()
                .meetingUuid(UUID.randomUUID()) // Generate a unique UUID for the session
                .shortTitle(request.getShortTitle())
                .description(request.getDescription())
                .host(host)
                .startTime(LocalDateTime.now())
                .isLive(true) // Starts as a live meeting
                .build();

        meeting = meetingRepository.save(meeting);

        // 3. Map to DTO and return
        return mapToDto(meeting);
    }

    /**
     * Ends a live meeting session.
     * @param meetingUuid The UUID of the meeting to end.
     */
    @Transactional
    public void endMeeting(UUID meetingUuid) {
        Meeting meeting = meetingRepository.findByMeetingUuid(meetingUuid)
                .orElseThrow(() -> new RuntimeException("Meeting not found with UUID: " + meetingUuid));

        if (meeting.isLive()) {
            meeting.setEndTime(LocalDateTime.now());
            meeting.setLive(false);
            meetingRepository.save(meeting);
        }
        // If it's already ended, silently succeed or throw a specific exception
    }

    /**
     * Retrieves a meeting by its unique UUID.
     * @param meetingUuid The unique identifier of the meeting.
     * @return The DTO of the requested meeting.
     */
    @Transactional(readOnly = true)
    public MeetingDto getMeetingByUuid(UUID meetingUuid) {
        Meeting meeting = meetingRepository.findByMeetingUuid(meetingUuid)
                .orElseThrow(() -> new RuntimeException("Meeting not found with UUID: " + meetingUuid));

        return mapToDto(meeting);
    }

    /**
     * Retrieves all currently live meetings.
     * @return A list of DTOs for live meetings.
     */
    @Transactional(readOnly = true)
    public List<MeetingDto> getLiveMeetings() {
        return meetingRepository.findAllByIsLive(true).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- Private Helper for Mapping ---

    private MeetingDto mapToDto(Meeting meeting) {
        // Ensure the host is loaded before accessing its properties (due to LAZY loading)
        String hostName = meeting.getHost().getFirstName() + " " + meeting.getHost().getLastName();

        return MeetingDto.builder()
                .meetingUuid(meeting.getMeetingUuid())
                .shortTitle(meeting.getShortTitle())
                .hostName(hostName)
                .startTime(meeting.getStartTime())
                .isLive(meeting.isLive())
                .build();
    }
}