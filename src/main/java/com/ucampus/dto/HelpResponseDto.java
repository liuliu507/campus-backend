package com.ucampus.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HelpResponseDto {
    private Long id;
    private String title;
    private String content;
    private String description;
    private String author;
    private String contact;
    private String status;
    private Boolean urgent;
    private Integer viewCount;
    private Integer replyCount;
    private String createdAt;
    private String category;
    private String location;
    private String urgency;
    private Double reward;
    private Long publisherId;
}