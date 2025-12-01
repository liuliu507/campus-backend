package com.ucampus.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "class_substitution")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSubstitution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 直接保存 publisherId，不再关联 User
    @Column(name = "publisher_id", nullable = false)
    private Long publisherId;

    @Column(name = "acceptor_id")
    private Long acceptorId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String courseName;

    private String teacher;

    @Column(nullable = false)
    private String campus;

    private String genderRequirement;

    @Column(nullable = false)
    private String classTime;

    @Column(nullable = false)
    private LocalDate classDate;

    @Column(nullable = false)
    private String courseType;

    @Column(nullable = false)
    private BigDecimal reward;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String contactInfo;

    @Builder.Default
    private String urgency = "一般";

    @Builder.Default
    private String status = "pending";

    @Builder.Default
    private Integer applicants = 0;

    @Builder.Default
    private Integer likes = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
