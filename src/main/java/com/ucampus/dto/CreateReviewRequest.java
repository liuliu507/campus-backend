// src/main/java/com/ucampus/dto/CreateReviewRequest.java
package com.ucampus.dto;

import lombok.Data;

@Data
public class CreateReviewRequest {
    private String targetType;
    private String targetName;
    private Integer rating;
    private String content;
    private String imagesUrl;
    private Long publisherId;
}