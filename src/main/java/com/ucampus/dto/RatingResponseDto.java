package com.ucampus.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponseDto {
    private Long id;
    private Long reporterId;
    private Long targetId;
    private String targetType;
    private String ratingType;
    private String reason;
    private String evidenceUrls;
    private String status;
    private String adminNote;
    private LocalDateTime createdAt;
    private LocalDateTime handledAt;
}
