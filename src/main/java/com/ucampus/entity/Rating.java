package com.ucampus.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rating_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 举报人 id（若无用户系统，可存匿名名称或 null）
    private Long reporterId;

    // 被举报对象 id（可为用户 id / 帖子 id / 活动 id 等）
    private Long targetId;

    // targetType 用于区分被举报对象类型，例如: "user", "post", "activity"
    private String targetType;

    // 评分类别： "red" 或 "black" 或 "neutral"（也可以用 enum）
    @Column(length = 20)
    private String ratingType;

    // 举报原因 / 说明
    @Column(length = 2000)
    private String reason;

    // 存证图片或链接（可多个，用逗号分隔），后续可改为单独表
    @Column(length = 2000)
    private String evidenceUrls;

    // 管理员处理状态: pending / accepted / rejected / dismissed
    @Column(length = 50)
    private String status = "pending";

    // 管理员备注
    @Column(length = 2000)
    private String adminNote;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime handledAt;
}
