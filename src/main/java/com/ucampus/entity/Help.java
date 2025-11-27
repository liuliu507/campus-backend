package com.ucampus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "help_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Help {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 求助标题
    @Column(nullable = false)
    private String title;

    // 求助内容 - 对应数据库的content字段
    @Column(nullable = false, length = 2000, name = "content")
    private String content;

    // 求助描述
    @Column(name = "description")
    private String description;

    // 发布者昵称
    private String author;

    // 联系方式
    private String contact;

    // 状态
    @Builder.Default
    private String status = "PENDING";

    // 是否紧急
    @Builder.Default
    private Boolean urgent = false;

    // 查看次数
    @Builder.Default
    private Integer viewCount = 0;

    // 回复次数
    @Builder.Default
    private Integer replyCount = 0;

    // 发布时间
    private String createdAt;

    // 分类
    private String category;

    // 地点
    private String location;

    // 紧急程度
    private String urgency;

    // 悬赏金额
    private Double reward;

    // 发布者ID
    private Long publisherId;
}