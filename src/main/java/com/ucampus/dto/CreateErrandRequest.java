// src/main/java/com/ucampus/dto/CreateErrandRequest.java
package com.ucampus.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateErrandRequest {
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
    // 移除 publisherId 字段，因为不再需要用户验证
}