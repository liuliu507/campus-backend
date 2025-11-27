package com.ucampus.controller;

import com.ucampus.dto.RatingRequestDto;
import com.ucampus.dto.RatingResponseDto;
import com.ucampus.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RatingController {

    private final RatingService ratingService;

    // 新建举报/打分记录
    @PostMapping("/create")
    public RatingResponseDto create(@RequestBody RatingRequestDto dto) {
        return ratingService.createRating(dto);
    }

    // 列出所有记录
    @GetMapping
    public List<RatingResponseDto> listAll() {
        return ratingService.listAll();
    }

    // 根据状态筛选，例如 ?status=pending
    @GetMapping("/by-status")
    public List<RatingResponseDto> listByStatus(@RequestParam String status) {
        return ratingService.listByStatus(status);
    }

    // 获取详情
    @GetMapping("/{id}")
    public RatingResponseDto getById(@PathVariable Long id) {
        return ratingService.getById(id);
    }

    // 管理员处理（accept/reject/dismiss）
    @PostMapping("/{id}/handle")
    public RatingResponseDto handle(@PathVariable Long id, @RequestParam String status, @RequestParam(required = false) String adminNote) {
        return ratingService.handleRating(id, status, adminNote);
    }

    // 删除记录
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ratingService.deleteById(id);
    }
}
