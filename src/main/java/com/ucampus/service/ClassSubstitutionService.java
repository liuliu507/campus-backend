package com.ucampus.service;

import com.ucampus.dto.ClassSubstitutionDTO;
import com.ucampus.dto.CreateSubstitutionRequest;
import com.ucampus.entity.ClassSubstitution;
import com.ucampus.repository.ClassSubstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassSubstitutionService {

    private final ClassSubstitutionRepository substitutionRepository;

    public List<ClassSubstitutionDTO> getAllSubstitutions() {
        return substitutionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ClassSubstitutionDTO> getPendingSubstitutions() {
        return substitutionRepository.findByStatusOrderByCreatedAtDesc("pending")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClassSubstitutionDTO createSubstitution(CreateSubstitutionRequest request) {

        // 不检查用户是否存在，直接使用 publisherId
        ClassSubstitution substitution = ClassSubstitution.builder()
                .publisherId(request.getPublisherId())
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
                .createdAt(LocalDateTime.now())
                .build();

        ClassSubstitution saved = substitutionRepository.save(substitution);
        return convertToDTO(saved);
    }

    public ClassSubstitutionDTO acceptSubstitution(Long id, Long acceptorId) {

        ClassSubstitution substitution = substitutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("代课任务不存在"));

        // 不检查是否是有效用户
        substitution.setAcceptorId(acceptorId);
        substitution.setStatus("accepted");
        substitution.setApplicants(substitution.getApplicants() + 1);

        ClassSubstitution updated = substitutionRepository.save(substitution);

        return convertToDTO(updated);
    }

    public void likeSubstitution(Long id) {
        ClassSubstitution substitution = substitutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("代课任务不存在"));

        substitution.setLikes(substitution.getLikes() + 1);
        substitutionRepository.save(substitution);
    }

    private ClassSubstitutionDTO convertToDTO(ClassSubstitution s) {
        ClassSubstitutionDTO dto = new ClassSubstitutionDTO();

        dto.setId(s.getId());
        dto.setTitle(s.getTitle());
        dto.setCourseName(s.getCourseName());
        dto.setTeacher(s.getTeacher());
        dto.setCampus(s.getCampus());
        dto.setGenderRequirement(s.getGenderRequirement());
        dto.setClassTime(s.getClassTime());
        dto.setClassDate(s.getClassDate());
        dto.setCourseType(s.getCourseType());
        dto.setReward(s.getReward());
        dto.setDescription(s.getDescription());
        dto.setContactInfo(s.getContactInfo());
        dto.setUrgency(s.getUrgency());
        dto.setStatus(s.getStatus());
        dto.setApplicants(s.getApplicants());
        dto.setLikes(s.getLikes());

        // 不关联 User 表，所以 publisherName 用 publisherId 替代
        dto.setPublisherName("用户 " + s.getPublisherId());

        dto.setCreatedAt(s.getCreatedAt());
        dto.setTimeAgo(calculateTimeAgo(s.getCreatedAt()));

        return dto;
    }

    private String calculateTimeAgo(LocalDateTime createdAt) {
        long minutes = ChronoUnit.MINUTES.between(createdAt, LocalDateTime.now());
        if (minutes < 60) return minutes + "分钟前";

        long hours = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        if (hours < 24) return hours + "小时前";

        long days = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
        return days + "天前";
    }
}
