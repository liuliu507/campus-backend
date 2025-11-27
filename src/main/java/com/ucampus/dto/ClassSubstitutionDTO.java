// src/main/java/com/ucampus/dto/ClassSubstitutionDTO.java
package com.ucampus.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
public class ClassSubstitutionDTO {
    private Long id;
    private String title;
    private String courseName;
    private String teacher;
    private String campus;
    private String genderRequirement;
    private String classTime;
    private LocalDate classDate;
    private String courseType;
    private BigDecimal reward;
    private String description;
    private String contactInfo;
    private String urgency;
    private String status;
    private Integer applicants;
    private Integer likes;
    private String publisherName;
    private LocalDateTime createdAt;
    private String timeAgo;
}