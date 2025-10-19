package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingStartRequest {
    private String hostId; // ID of the UserProfile hosting the call
    private String shortTitle;
    private String description;
}