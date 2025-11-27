package com.ucampus.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobDTO {
    private Long id;
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
    private String publisherId;
    private String publisherName;
    private List<String> images;
    private Boolean urgent;
    private String status;
    private Integer viewCount;
    private Integer applyCount;
    private LocalDateTime createdAt;
    private LocalDateTime expireDate;
    private String timeAgo;
    private String daysLeft;

    // 计算相对时间
    public String calculateTimeAgo() {
        if (createdAt == null) return "";

        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(createdAt, now).toMinutes();
        long hours = java.time.Duration.between(createdAt, now).toHours();
        long days = java.time.Duration.between(createdAt, now).toDays();

        if (minutes < 1) return "刚刚";
        if (minutes < 60) return minutes + "分钟前";
        if (hours < 24) return hours + "小时前";
        if (days < 30) return days + "天前";
        return "1个月前";
    }

    // 计算剩余天数
    public String calculateDaysLeft() {
        if (expireDate == null) return "";

        LocalDateTime now = LocalDateTime.now();
        long days = java.time.Duration.between(now, expireDate).toDays();

        if (days < 0) return "已过期";
        if (days == 0) return "今天截止";
        if (days == 1) return "1天后截止";
        return days + "天后截止";
    }
}