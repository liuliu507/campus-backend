package com.ucampus.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelpRequestDto {
    private String title;
    private String content;
    private String description;
    private String author;
    private String contact;
    private String status;
    private Boolean urgent;
    private String category;
    private String location;
    private String urgency;
    private Double reward;
    private Long publisherId;
}