// src/main/java/com/ucampus/entity/User.java
package com.ucampus.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    private String phone;
    private String avatarUrl;

    @Builder.Default
    private Integer creditScore = 100;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}