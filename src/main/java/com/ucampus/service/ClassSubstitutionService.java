// src/main/java/com/ucampus/service/ClassSubstitutionService.java
package com.ucampus.service;

import com.ucampus.dto.ClassSubstitutionDTO;
import com.ucampus.dto.CreateSubstitutionRequest;
import com.ucampus.entity.ClassSubstitution;
import com.ucampus.entity.User;
import com.ucampus.repository.ClassSubstitutionRepository;
import com.ucampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassSubstitutionService {

    private final ClassSubstitutionRepository substitutionRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ClassSubstitutionService.class);

    public List<ClassSubstitutionDTO> getAllSubstitutions() {
        logger.info("获取所有代课需求");
        return substitutionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ClassSubstitutionDTO> getPendingSubstitutions() {
        logger.info("获取待处理代课需求");
        return substitutionRepository.findByStatusOrderByCreatedAtDesc("pending")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClassSubstitutionDTO createSubstitution(CreateSubstitutionRequest request) {
        logger.info("开始创建代课需求，发布者ID: {}", request.getPublisherId());

        try {
            // 完全移除用户检查 - 直接创建代课需求
            logger.info("跳过用户检查，直接创建代课需求");

            ClassSubstitution substitution = ClassSubstitution.builder()
                    // 不再设置 publisher，或者设置为 null
                    // .publisher(null)
                    .title(request.getTitle())
                    .courseName(request.getCourseName())
                    .teacher(request.getTeacher())
                    .campus(request.getCampus())
                    .genderRequirement(request.getGenderRequirement())
                    .classTime(request.getClassTime())
                    .classDate(request.getClassDate())
                    .courseType(request.getCourseType())
                    .reward(request.getReward())
                    .description(request.getDescription())
                    .contactInfo(request.getContactInfo())
                    .urgency(request.getUrgency())
                    .status("pending")
                    .applicants(0)
                    .likes(0)
                    .createdAt(LocalDateTime.now())
                    .build();

            ClassSubstitution saved = substitutionRepository.save(substitution);
            logger.info("代课需求创建成功，ID: {}", saved.getId());

            return convertToDTO(saved);

        } catch (Exception e) {
            logger.error("创建代课需求时发生错误", e);
            throw new RuntimeException("创建代课需求失败: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ClassSubstitutionDTO acceptSubstitution(Long substitutionId, Long acceptorId) {
        logger.info("接单操作 - 代课ID: {}, 接单者ID: {}", substitutionId, acceptorId);

        ClassSubstitution substitution = substitutionRepository.findById(substitutionId)
                .orElseThrow(() -> new RuntimeException("代课任务不存在"));

        // 跳过接单者用户检查
        logger.info("跳过接单者用户检查");

        substitution.setStatus("accepted");
        substitution.setApplicants(substitution.getApplicants() + 1);

        ClassSubstitution updated = substitutionRepository.save(substitution);
        logger.info("接单成功，代课ID: {}", substitutionId);

        return convertToDTO(updated);
    }

    public void likeSubstitution(Long substitutionId) {
        ClassSubstitution substitution = substitutionRepository.findById(substitutionId)
                .orElseThrow(() -> new RuntimeException("代课任务不存在"));

        substitution.setLikes(substitution.getLikes() + 1);
        substitutionRepository.save(substitution);
    }

    private ClassSubstitutionDTO convertToDTO(ClassSubstitution substitution) {
        ClassSubstitutionDTO dto = new ClassSubstitutionDTO();
        dto.setId(substitution.getId());
        dto.setTitle(substitution.getTitle());
        dto.setCourseName(substitution.getCourseName());
        dto.setTeacher(substitution.getTeacher());
        dto.setCampus(substitution.getCampus());
        dto.setGenderRequirement(substitution.getGenderRequirement());
        dto.setClassTime(substitution.getClassTime());
        dto.setClassDate(substitution.getClassDate());
        dto.setCourseType(substitution.getCourseType());
        dto.setReward(substitution.getReward());
        dto.setDescription(substitution.getDescription());
        dto.setContactInfo(substitution.getContactInfo());
        dto.setUrgency(substitution.getUrgency());
        dto.setStatus(substitution.getStatus());
        dto.setApplicants(substitution.getApplicants());
        dto.setLikes(substitution.getLikes());

        // 修改这里：如果没有发布者，使用默认值
        // dto.setPublisherName(substitution.getPublisher() != null ?
        //     substitution.getPublisher().getUsername() : "匿名用户");
        dto.setPublisherName("匿名用户"); // 直接使用固定值

        dto.setCreatedAt(substitution.getCreatedAt());
        dto.setTimeAgo(calculateTimeAgo(substitution.getCreatedAt()));

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