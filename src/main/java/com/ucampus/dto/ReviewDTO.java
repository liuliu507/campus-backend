// src/main/java/com/ucampus/dto/ReviewDTO.java
package com.ucampus.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private String targetType;
    private String targetName;
    private Integer rating;
    private String content;
    private String imagesUrl;
    private Integer likes;
    private String publisherName;
    private LocalDateTime createdAt;
    private String timeAgo;
}