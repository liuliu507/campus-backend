package com.ucampus.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shares")
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;       // 发布者的用户ID
    private String content;    // 趣事内容
    private String imageUrl;   // 图片链接，可为空

    private LocalDateTime createdAt;
}
