// src/main/java/com/ucampus/entity/Errand.java
package com.ucampus.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "errands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Errand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publisher_id")
    private Long publisherId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(name = "from_location", nullable = false)
    private String fromLocation;

    @Column(name = "to_location", nullable = false)
    private String toLocation;

    @Column(nullable = false)
    private BigDecimal price;

    private String weight;
    private String quantity;

    @Column(nullable = false)
    private String deadline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Builder.Default
    private String urgency = "一般";

    @Builder.Default
    private String status = "pending";

    private String distance;
    private String estimatedTime;

    @Builder.Default
    private Integer applicants = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}