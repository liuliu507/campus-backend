// src/main/java/com/ucampus/dto/CreateHelpRequest.java
package com.ucampus.dto;

import lombok.Data;

@Data
public class CreateHelpRequest {
    private String title;
    private String content;
    private String category;
    private String location;
    private String urgency;
    private Double reward;
    private String contactInfo;
    private Long publisherId;
}