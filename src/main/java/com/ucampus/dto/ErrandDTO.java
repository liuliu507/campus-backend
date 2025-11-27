// src/main/java/com/ucampus/dto/ErrandDTO.java
package com.ucampus.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ErrandDTO {
    private Long id;
    private String title;
    private String category;
    private String fromLocation;
    private String toLocation;
    private BigDecimal price;
    private String weight;
    private String quantity;
    private String deadline;
    private String description;
    private String contactInfo;
    private String urgency;
    private String status;
    private String distance;
    private String estimatedTime;
    private Integer applicants;
    private String publisherName;
    private LocalDateTime createdAt;
    private String timeAgo;
}