package com.ucampus.dto;

import lombok.Data;

@Data
public class ShareDto {
    private Long userId;
    private String content;
    private String imageUrl;  // 可选
}
