package com.ucampus.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateSecondhandRequest {
    private String title;
    private String description;
    private Double price;
    private Double originalPrice;
    private String category;
    private String condition;
    private String location;
    private String contact;
    private Boolean urgent = false;
    private List<String> images;

    // 移除不需要的字段
    // private String drive;
}