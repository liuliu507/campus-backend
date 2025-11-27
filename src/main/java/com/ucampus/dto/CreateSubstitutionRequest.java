// src/main/java/com/ucampus/dto/CreateSubstitutionRequest.java
package com.ucampus.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateSubstitutionRequest {
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
    private Long publisherId;
}