// src/main/java/com/ucampus/service/ErrandService.java
package com.ucampus.service;

import com.ucampus.dto.ErrandDTO;
import com.ucampus.dto.CreateErrandRequest;
import com.ucampus.entity.Errand;
import com.ucampus.entity.User;
import com.ucampus.repository.ErrandRepository;
import com.ucampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ErrandService {

    private final ErrandRepository errandRepository;
    private final UserRepository userRepository;

    public List<ErrandDTO> getAllErrands() {
        return errandRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ErrandDTO> getPendingErrands() {
        return errandRepository.findByStatusOrderByCreatedAtDesc("pending")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ErrandDTO createErrand(CreateErrandRequest request) {
        try {
            System.out.println("接收到创建跑腿任务请求: " + request.toString());

            // 创建匿名跑腿任务，完全不设置 publisher
            Errand errand = Errand.builder()
                    .title(request.getTitle())
                    .category(request.getCategory())
                    .fromLocation(request.getFromLocation())
                    .toLocation(request.getToLocation())
                    .price(request.getPrice())
                    .weight(request.getWeight())
                    .quantity(request.getQuantity())
                    .deadline(request.getDeadline())
                    .description(request.getDescription())
                    .contactInfo(request.getContactInfo())
                    .urgency(request.getUrgency() != null ? request.getUrgency() : "一般")
                    .status("pending")
                    .applicants(0)
                    .createdAt(LocalDateTime.now())
                    .build();

            // 重要：不设置 publisher，让数据库自动设置为 NULL
            // errand.setPublisher(null); // 或者直接注释掉这行

            System.out.println("创建 Errand 实体: " + errand.toString());

            Errand saved = errandRepository.save(errand);
            System.out.println("保存成功，ID: " + saved.getId());

            return convertToDTO(saved);

        } catch (Exception e) {
            System.err.println("创建跑腿任务失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("创建跑腿任务失败: " + e.getMessage());
        }
    }

    public ErrandDTO acceptErrand(Long errandId, Long acceptorId) {
        Errand errand = errandRepository.findById(errandId)
                .orElseThrow(() -> new RuntimeException("跑腿任务不存在"));

        errand.setApplicants(errand.getApplicants() + 1);
        Errand updated = errandRepository.save(errand);
        return convertToDTO(updated);
    }




    private ErrandDTO convertToDTO(Errand errand) {
        ErrandDTO dto = new ErrandDTO();
        dto.setId(errand.getId());
        dto.setTitle(errand.getTitle());
        dto.setCategory(errand.getCategory());
        dto.setFromLocation(errand.getFromLocation());
        dto.setToLocation(errand.getToLocation());
        dto.setPrice(errand.getPrice());
        dto.setWeight(errand.getWeight());
        dto.setQuantity(errand.getQuantity());
        dto.setDeadline(errand.getDeadline());
        dto.setDescription(errand.getDescription());
        dto.setContactInfo(errand.getContactInfo());
        dto.setUrgency(errand.getUrgency());
        dto.setStatus(errand.getStatus());
        dto.setApplicants(errand.getApplicants());

        // 直接设置发布者名称为"匿名用户"
        dto.setPublisherName("匿名用户");

        dto.setCreatedAt(errand.getCreatedAt());
        dto.setTimeAgo(calculateTimeAgo(errand.getCreatedAt()));

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