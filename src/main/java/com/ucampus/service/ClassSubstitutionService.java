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
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassSubstitutionService {

    private final ClassSubstitutionRepository substitutionRepository;
    private final UserRepository userRepository;

    // 添加详细日志
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
        logger.info("请求数据 - 标题: {}, 校区: {}, 课程类型: {}",
                request.getTitle(), request.getCampus(), request.getCourseType());

        try {
            // 详细记录查找用户的过程
            logger.info("查找用户 ID: {}", request.getPublisherId());
            User publisher = userRepository.findById(request.getPublisherId())
                    .orElseGet(() -> {
                        logger.warn("用户ID {} 不存在，开始创建默认用户", request.getPublisherId());
                        User defaultUser = createDefaultUser();
                        logger.info("默认用户创建成功，新用户ID: {}", defaultUser.getId());
                        return defaultUser;
                    });

            logger.info("找到发布者: {} (ID: {})", publisher.getUsername(), publisher.getId());

            // 创建代课需求
            ClassSubstitution substitution = ClassSubstitution.builder()
                    .publisher(publisher)
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

            logger.info("保存代课需求到数据库");
            ClassSubstitution saved = substitutionRepository.save(substitution);
            logger.info("代课需求创建成功，ID: {}", saved.getId());

            return convertToDTO(saved);

        } catch (Exception e) {
            logger.error("创建代课需求时发生严重错误", e);
            throw new RuntimeException("创建代课需求失败: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ClassSubstitutionDTO acceptSubstitution(Long substitutionId, Long acceptorId) {
        logger.info("接单操作 - 代课ID: {}, 接单者ID: {}", substitutionId, acceptorId);

        ClassSubstitution substitution = substitutionRepository.findById(substitutionId)
                .orElseThrow(() -> {
                    logger.error("代课任务不存在: {}", substitutionId);
                    return new RuntimeException("代课任务不存在");
                });

        logger.info("查找接单用户 ID: {}", acceptorId);
        User acceptor = userRepository.findById(acceptorId)
                .orElseGet(() -> {
                    logger.warn("接单用户ID {} 不存在，创建默认用户", acceptorId);
                    User defaultUser = createDefaultUser();
                    logger.info("接单默认用户创建成功，新用户ID: {}", defaultUser.getId());
                    return defaultUser;
                });

        substitution.setAcceptor(acceptor);
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

        logger.info("点赞成功，代课ID: {}", substitutionId);
    }

    private User createDefaultUser() {
        try {
            logger.info("开始创建默认用户");

            // 使用更唯一的标识，避免约束冲突
            String timestamp = String.valueOf(System.currentTimeMillis());
            String randomSuffix = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999));
            String uniqueId = timestamp + "_" + randomSuffix;

            // 生成唯一的学生ID、用户名和邮箱
            String studentId = "temp_user_" + uniqueId;
            String username = "临时用户_" + uniqueId;
            String email = "temp_user_" + uniqueId + "@example.com";

            logger.info("创建用户 - 学号: {}, 用户名: {}, 邮箱: {}", studentId, username, email);

            User defaultUser = User.builder()
                    .studentId(studentId)
                    .username(username)
                    .email(email)
                    .phone("13800138000") // 使用固定电话避免约束
                    .avatarUrl("")
                    .creditScore(100)
                    .createdAt(LocalDateTime.now())
                    .build();

            User savedUser = userRepository.save(defaultUser);
            logger.info("默认用户创建成功，ID: {}, 学号: {}", savedUser.getId(), savedUser.getStudentId());

            return savedUser;

        } catch (Exception e) {
            logger.error("创建默认用户时发生错误", e);

            // 尝试使用备用方案
            try {
                logger.info("尝试备用用户创建方案");
                return createFallbackUser();
            } catch (Exception fallbackException) {
                logger.error("备用用户创建也失败", fallbackException);

                // 最后尝试：查找系统中已存在的任何用户
                logger.info("尝试查找系统中已存在的用户");
                List<User> existingUsers = userRepository.findAll();
                if (!existingUsers.isEmpty()) {
                    User firstUser = existingUsers.get(0);
                    logger.info("使用系统中第一个用户作为发布者: {} (ID: {})",
                            firstUser.getUsername(), firstUser.getId());
                    return firstUser;
                } else {
                    logger.error("系统中没有任何用户，无法继续");
                    throw new RuntimeException("无法创建用户且系统中无任何可用用户");
                }
            }
        }
    }

    private User createFallbackUser() {
        // 使用UUID确保唯一性
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String studentId = "fb_" + uuid;
        String username = "备用用户_" + uuid;
        String email = "fallback_" + uuid + "@example.com";

        logger.info("创建备用用户 - 学号: {}, 用户名: {}, 邮箱: {}", studentId, username, email);

        User fallbackUser = User.builder()
                .studentId(studentId)
                .username(username)
                .email(email)
                .phone("13800138000")
                .avatarUrl("")
                .creditScore(100)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(fallbackUser);
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
        dto.setPublisherName(substitution.getPublisher().getUsername());
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