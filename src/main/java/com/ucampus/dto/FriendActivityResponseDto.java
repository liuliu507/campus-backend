package com.ucampus.dto;

import java.time.LocalDateTime;
import java.util.List;

public class FriendActivityResponseDto {
    public Long id;
    public String title;
    public String type;
    public String activity;
    public String location;
    public String timeText;
    public String people;
    public Integer maxParticipants;
    public String description;
    public String contact;
    public String urgency;
    public String gender;
    public List<String> tags;
    public Integer participants;
    public LocalDateTime createdAt;
}
