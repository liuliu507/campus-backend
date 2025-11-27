package com.ucampus.service;

import com.ucampus.dto.RatingRequestDto;
import com.ucampus.dto.RatingResponseDto;

import java.util.List;

public interface RatingService {
    RatingResponseDto createRating(RatingRequestDto request);
    List<RatingResponseDto> listAll();
    List<RatingResponseDto> listByStatus(String status);
    RatingResponseDto getById(Long id);
    RatingResponseDto handleRating(Long id, String newStatus, String adminNote); // 管理员处理
    void deleteById(Long id);
}
