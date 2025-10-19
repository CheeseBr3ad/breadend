package com.example.demo.controller;

// MeetingController.java

import com.example.demo.dto.MeetingDto;
import com.example.demo.dto.MeetingStartRequest;
import com.example.demo.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    /**
     * POST /api/v1/meetings
     * Starts a new meeting session.
     */
    @PostMapping
    public ResponseEntity<MeetingDto> startMeeting(
            @AuthenticationPrincipal User user, @RequestBody MeetingStartRequest request) {
        String email = user.getUsername();
        request.setHostId(email);
        MeetingDto newMeeting = meetingService.startMeeting(request);
        return new ResponseEntity<>(newMeeting, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/meetings/{meetingUuid}
     * Retrieves the details of a specific meeting.
     */
    @GetMapping("/{meetingUuid}")
    public ResponseEntity<MeetingDto> getMeeting(@PathVariable UUID meetingUuid) {
        MeetingDto meetingDto = meetingService.getMeetingByUuid(meetingUuid);
        return ResponseEntity.ok(meetingDto);
    }

    /**
     * GET /api/v1/meetings/live
     * Retrieves a list of all currently active (live) meetings.
     */
    @GetMapping("/live")
    public ResponseEntity<List<MeetingDto>> getLiveMeetings() {
        List<MeetingDto> liveMeetings = meetingService.getLiveMeetings();
        return ResponseEntity.ok(liveMeetings);
    }

    /**
     * PUT /api/v1/meetings/{meetingUuid}/end
     * Ends an active meeting session.
     */
    @PutMapping("/{meetingUuid}/end")
    public ResponseEntity<Void> endMeeting(@PathVariable UUID meetingUuid) {
        meetingService.endMeeting(meetingUuid);
        return ResponseEntity.noContent().build(); // 204 No Content on successful update
    }

    // Note: Proper exception handling (e.g., @ControllerAdvice for RuntimeException/NotFound)
    // is omitted here for brevity but is essential in a production environment.
}