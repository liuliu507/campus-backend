package com.ucampus.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequestDto {
    private Long reporterId;       // 可选：举报人 id
    private Long targetId;         // 必需：被举报对象 id
    private String targetType;     // 必需：对象类型，例如 "user" / "post" / "activity"
    private String ratingType;     // 必需： "red" 或 "black"
    private String reason;         // 可选：详细说明
    private String evidenceUrls;   // 可选：多张图片链接用逗号分隔
}
