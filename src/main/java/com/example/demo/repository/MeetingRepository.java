package com.example.demo.repository;

// MeetingRepository.java


import com.example.demo.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    // Essential for retrieving a meeting by its external, unique identifier
    Optional<Meeting> findByMeetingUuid(UUID meetingUuid);

    // Essential for the service to list all currently "live" calls
    List<Meeting> findAllByIsLive(boolean isLive);

    // Useful for finding meetings hosted by a specific user
    List<Meeting> findAllByHostId(Long hostId);
}