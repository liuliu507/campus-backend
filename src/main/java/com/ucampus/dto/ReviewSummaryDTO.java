// src/main/java/com/ucampus/dto/ReviewSummaryDTO.java
package com.ucampus.dto;

import lombok.Data;

@Data
public class ReviewSummaryDTO {
    private String targetName;
    private String targetType;
    private Double averageRating;
    private Integer reviewCount;
    private Integer fiveStarCount;
    private Integer fourStarCount;
    private Integer threeStarCount;
    private Integer twoStarCount;
    private Integer oneStarCount;
}