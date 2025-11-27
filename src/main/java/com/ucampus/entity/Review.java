// src/main/java/com/ucampus/entity/Review.java
package com.ucampus.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publisher_id", nullable = false)
    private Long publisherId;

    @Column(name = "target_type", nullable = false, length = 50)
    private String targetType; // "COURSE" 或 "MERCHANT"

    @Column(name = "target_name", nullable = false, length = 200)
    private String targetName;

    @Column(nullable = false)
    private Integer rating; // 1-5分

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "images_url", length = 500)
    private String imagesUrl;

    @Builder.Default
    private Integer likes = 0;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}