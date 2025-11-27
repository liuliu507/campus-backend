// src/main/java/com/ucampus/service/ClassSubstitutionService.java
package com.ucampus.service;

import com.ucampus.dto.ClassSubstitutionDTO;
import com.ucampus.dto.CreateSubstitutionRequest;
import com.ucampus.entity.ClassSubstitution;
import com.ucampus.entity.User;
import com.ucampus.repository.ClassSubstitutionRepository;
import com.ucampus.repository.UserRepository;
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
    private final UserRepository userRepository;

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
        User publisher = userRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

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

        ClassSubstitution saved = substitutionRepository.save(substitution);
        return convertToDTO(saved);
    }

    public ClassSubstitutionDTO acceptSubstitution(Long substitutionId, Long acceptorId) {
        ClassSubstitution substitution = substitutionRepository.findById(substitutionId)
                .orElseThrow(() -> new RuntimeException("代课任务不存在"));

        User acceptor = userRepository.findById(acceptorId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        substitution.setAcceptor(acceptor);
        substitution.setStatus("accepted");
        substitution.setApplicants(substitution.getApplicants() + 1);

        ClassSubstitution updated = substitutionRepository.save(substitution);
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