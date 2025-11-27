// src/main/java/com/ucampus/repository/ReviewRepository.java
package com.ucampus.repository;

import com.ucampus.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 根据目标类型和目标名称查找评价
    List<Review> findByTargetTypeAndTargetNameOrderByCreatedAtDesc(String targetType, String targetName);

    // 根据发布者ID查找评价
    List<Review> findByPublisherIdOrderByCreatedAtDesc(Long publisherId);

    // 根据目标类型查找评价
    List<Review> findByTargetTypeOrderByCreatedAtDesc(String targetType);

    // 搜索评价
    @Query("SELECT r FROM Review r WHERE " +
            "LOWER(r.targetName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Review> searchByKeyword(@Param("keyword") String keyword);

    // 获取某个目标的平均评分
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.targetType = :targetType AND r.targetName = :targetName")
    Double findAverageRatingByTarget(@Param("targetType") String targetType,
                                     @Param("targetName") String targetName);
}