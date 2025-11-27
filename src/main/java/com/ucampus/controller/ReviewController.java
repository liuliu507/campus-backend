// src/main/java/com/ucampus/controller/ReviewController.java
package com.ucampus.controller;

import com.ucampus.dto.RedBlackRequest;
import com.ucampus.dto.ReviewDTO;
import com.ucampus.dto.CreateReviewRequest;
import com.ucampus.dto.ReviewSummaryDTO;
import com.ucampus.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/target")
    public ResponseEntity<List<ReviewDTO>> getReviewsByTarget(
            @RequestParam String targetType,
            @RequestParam String targetName) {
        List<ReviewDTO> reviews = reviewService.getReviewsByTarget(targetType, targetName);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByPublisher(@PathVariable Long publisherId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByPublisher(publisherId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/type/{targetType}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByType(@PathVariable String targetType) {
        List<ReviewDTO> reviews = reviewService.getReviewsByType(targetType);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReviewDTO>> searchReviews(@RequestParam String keyword) {
        List<ReviewDTO> reviews = reviewService.searchReviews(keyword);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody CreateReviewRequest request) {
        ReviewDTO created = reviewService.createReview(request);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ReviewDTO> likeReview(@PathVariable Long id) {
        ReviewDTO updated = reviewService.likeReview(id);
        return ResponseEntity.ok(updated);
    }

    // 在 ReviewController 中添加以下方法：

    @PostMapping("/red-black")
    public ResponseEntity<ReviewDTO> createRedBlackReview(@RequestBody RedBlackRequest request) {
        CreateReviewRequest reviewRequest = new CreateReviewRequest();
        reviewRequest.setTargetType("红黑榜");
        reviewRequest.setTargetName(request.getTitle());
        // 红榜=5分，黑榜=1分
        reviewRequest.setRating("red".equals(request.getType()) ? 5 : 1);
        reviewRequest.setContent(request.getContent());
        reviewRequest.setPublisherId(request.getPublisherId());
        reviewRequest.setImagesUrl("");

        ReviewDTO created = reviewService.createReview(reviewRequest);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/red-black")
    public ResponseEntity<List<ReviewDTO>> getRedBlackReviews() {
        // 只返回红黑榜类型的评价
        List<ReviewDTO> reviews = reviewService.getReviewsByType("红黑榜");
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/summary")
    public ResponseEntity<ReviewSummaryDTO> getReviewSummary(
            @RequestParam String targetType,
            @RequestParam String targetName) {
        ReviewSummaryDTO summary = reviewService.getReviewSummary(targetType, targetName);
        return ResponseEntity.ok(summary);
    }
}