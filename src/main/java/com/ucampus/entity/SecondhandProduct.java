package com.ucampus.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "secondhand_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecondhandProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String condition;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String contact;

    @Column(name = "seller_id") // 移除 nullable = false 允许匿名发布
    private String sellerId;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "image_urls")
    private String imageUrls;

    @Column(nullable = false)
    private Boolean urgent = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.AVAILABLE;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        // 确保必填字段有默认值
        if (this.sellerId == null) {
            this.sellerId = "anonymous_" + System.currentTimeMillis();
        }
        if (this.sellerName == null) {
            this.sellerName = "匿名用户";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ProductStatus {
        AVAILABLE,   // 可交易
        RESERVED,    // 已预订
        SOLD,        // 已售出
        EXPIRED      // 已过期
    }
}