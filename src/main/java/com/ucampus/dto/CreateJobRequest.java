package com.ucampus.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateJobRequest {
    private String title;
    private String description;
    private String company;
    private String jobType;
    private String category;
    private String salary;
    private String location;
    private String workAddress;
    private String contactInfo;
    private String contactPerson;
    private String requirements;
    private String benefits;
    private String workHours;
    private Boolean urgent = false;
    private List<String> images;
    private String drive;
}