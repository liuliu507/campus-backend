package com.ucampus.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
public class SecondhandProductDTO {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Double originalPrice;
    private String category;
    private String condition;
    private String location;
    private String contact;
    private String sellerId;
    private String sellerName;
    private List<String> images;
    private Boolean urgent;
    private String status;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private String timeAgo;

    // 计算相对时间的方法
    public String calculateTimeAgo() {
        if (createdAt == null) return "未知时间";

        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(createdAt, now);
        long hours = ChronoUnit.HOURS.between(createdAt, now);
        long days = ChronoUnit.DAYS.between(createdAt, now);

        if (minutes < 1) return "刚刚";
        if (minutes < 60) return minutes + "分钟前";
        if (hours < 24) return hours + "小时前";
        if (days < 30) return days + "天前";
        return "1个月前";
    }
}