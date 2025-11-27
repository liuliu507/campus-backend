package com.ucampus.dto;

import java.util.List;

public class FriendActivityDto {
    public String title;
    public String type;
    public String activity;
    public String location;
    public String timeText;
    public String people;
    public Integer maxParticipants; // 0 表示不限
    public String description;
    public String contact;
    public String urgency;
    public String gender;
    public List<String> tags;
}
