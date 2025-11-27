package com.ucampus.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String jobType; // 兼职/实习/全职

    @Column(nullable = false)
    private String category; // 岗位类别

    @Column(nullable = false)
    private String salary; // 薪资范围

    @Column(nullable = false)
    private String location;

    @Column(name = "work_address")
    private String workAddress;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;

    @Column(name = "work_hours")
    private String workHours;

    @Column(name = "publisher_id", nullable = false)
    private String publisherId;

    @Column(name = "publisher_name")
    private String publisherName;

    @Column(name = "image_urls")
    private String imageUrls;

    @Column(nullable = false)
    private Boolean urgent = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.OPEN;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "apply_count")
    private Integer applyCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // 默认30天后过期
        expireDate = LocalDateTime.now().plusDays(30);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum JobStatus {
        OPEN,        // 招聘中
        CLOSED,      // 已结束
        FILLED       // 已招满
    }
}