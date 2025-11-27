package com.ucampus.dto;

import lombok.Data;

@Data
public class JobApplicationRequest {
    private Long jobId;
    private String applicantName;
    private String applicantContact;
    private String applicantMajor;
    private String applicantGrade;
    private String resume;
    private String message;
}