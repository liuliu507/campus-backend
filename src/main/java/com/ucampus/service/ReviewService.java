// src/main/java/com/ucampus/service/ReviewService.java
package com.ucampus.service;

import com.ucampus.dto.ReviewDTO;
import com.ucampus.dto.CreateReviewRequest;
import com.ucampus.dto.ReviewSummaryDTO;
import com.ucampus.entity.Review;
import com.ucampus.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByTarget(String targetType, String targetName) {
        return reviewRepository.findByTargetTypeAndTargetNameOrderByCreatedAtDesc(targetType, targetName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByPublisher(Long publisherId) {
        return reviewRepository.findByPublisherIdOrderByCreatedAtDesc(publisherId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByType(String targetType) {
        return reviewRepository.findByTargetTypeOrderByCreatedAtDesc(targetType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> searchReviews(String keyword) {
        return reviewRepository.searchByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO createReview(CreateReviewRequest request) {
        // 验证评分范围
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("评分必须在1-5之间");
        }

        Review review = Review.builder()
                .publisherId(request.getPublisherId())
                .targetType(request.getTargetType())
                .targetName(request.getTargetName())
                .rating(request.getRating())
                .content(request.getContent())
                .imagesUrl(request.getImagesUrl())
                .likes(0)
                .createdAt(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);
        return convertToDTO(saved);
    }

    public ReviewDTO likeReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("评价不存在"));

        review.setLikes(review.getLikes() + 1);
        Review updated = reviewRepository.save(review);
        return convertToDTO(updated);
    }

    // 删除评价方法
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("评价不存在"));
        reviewRepository.delete(review);
    }

    // 根据发布者ID删除评价（权限验证）
    public void deleteReviewByPublisher(Long reviewId, Long publisherId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("评价不存在"));

        // 验证发布者权限
        if (!review.getPublisherId().equals(publisherId)) {
            throw new RuntimeException("无权删除此评价");
        }

        reviewRepository.delete(review);
    }

    public ReviewSummaryDTO getReviewSummary(String targetType, String targetName) {
        ReviewSummaryDTO summary = new ReviewSummaryDTO();
        summary.setTargetName(targetName);
        summary.setTargetType(targetType);

        // 获取平均评分
        Double avgRating = reviewRepository.findAverageRatingByTarget(targetType, targetName);
        summary.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);

        // 获取评价列表用于统计
        List<Review> reviews = reviewRepository.findByTargetTypeAndTargetNameOrderByCreatedAtDesc(targetType, targetName);
        summary.setReviewCount(reviews.size());

        // 统计各星级数量
        summary.setFiveStarCount(0);
        summary.setFourStarCount(0);
        summary.setThreeStarCount(0);
        summary.setTwoStarCount(0);
        summary.setOneStarCount(0);

        for (Review review : reviews) {
            switch (review.getRating()) {
                case 5: summary.setFiveStarCount(summary.getFiveStarCount() + 1); break;
                case 4: summary.setFourStarCount(summary.getFourStarCount() + 1); break;
                case 3: summary.setThreeStarCount(summary.getThreeStarCount() + 1); break;
                case 2: summary.setTwoStarCount(summary.getTwoStarCount() + 1); break;
                case 1: summary.setOneStarCount(summary.getOneStarCount() + 1); break;
            }
        }

        return summary;
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setTargetType(review.getTargetType());
        dto.setTargetName(review.getTargetName());
        dto.setRating(review.getRating());
        dto.setContent(review.getContent());
        dto.setImagesUrl(review.getImagesUrl());
        dto.setLikes(review.getLikes());
        dto.setPublisherName("用户" + review.getPublisherId()); // 简化处理
        dto.setCreatedAt(review.getCreatedAt());
        dto.setTimeAgo(calculateTimeAgo(review.getCreatedAt()));
        // 注意：这里暂时注释掉，因为ReviewDTO可能没有setPublisherId方法
        // dto.setPublisherId(review.getPublisherId());

        return dto;
    }

    private String calculateTimeAgo(LocalDateTime createdAt) {
        long minutes = ChronoUnit.MINUTES.between(createdAt, LocalDateTime.now());
        if (minutes < 60) {
            return minutes + "分钟前";
        }
        long hours = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        if (hours < 24) {
            return hours + "小时前";
        }
        long days = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
        return days + "天前";
    }
}