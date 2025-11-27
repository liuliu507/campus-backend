package com.ucampus.service;

import com.ucampus.dto.RatingRequestDto;
import com.ucampus.dto.RatingResponseDto;
import com.ucampus.entity.Rating;
import com.ucampus.repository.RatingRepository;
import com.ucampus.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository repository;

    @Override
    public RatingResponseDto createRating(RatingRequestDto request) {
        Rating entry = Rating.builder()
                .reporterId(request.getReporterId())
                .targetId(request.getTargetId())
                .targetType(request.getTargetType())
                .ratingType(request.getRatingType())
                .reason(request.getReason())
                .evidenceUrls(request.getEvidenceUrls())
                .status("pending")
                .createdAt(LocalDateTime.now())
                .build();

        Rating saved = repository.save(entry);
        return toDto(saved);
    }

    @Override
    public List<RatingResponseDto> listAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RatingResponseDto> listByStatus(String status) {
        return repository.findByStatus(status).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public RatingResponseDto getById(Long id) {
        return repository.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("Rating not found: " + id));
    }

    @Override
    public RatingResponseDto handleRating(Long id, String newStatus, String adminNote) {
        Rating entry = repository.findById(id).orElseThrow(() -> new RuntimeException("Rating not found: " + id));
        entry.setStatus(newStatus);
        entry.setAdminNote(adminNote);
        entry.setHandledAt(LocalDateTime.now());
        Rating saved = repository.save(entry);

        // 可选：在这里做后续动作，例如更改被举报对象的可见性、计数等

        return toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private RatingResponseDto toDto(Rating e) {
        return RatingResponseDto.builder()
                .id(e.getId())
                .reporterId(e.getReporterId())
                .targetId(e.getTargetId())
                .targetType(e.getTargetType())
                .ratingType(e.getRatingType())
                .reason(e.getReason())
                .evidenceUrls(e.getEvidenceUrls())
                .status(e.getStatus())
                .adminNote(e.getAdminNote())
                .createdAt(e.getCreatedAt())
                .handledAt(e.getHandledAt())
                .build();
    }
}
