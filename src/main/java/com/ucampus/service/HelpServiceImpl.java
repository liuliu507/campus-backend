package com.ucampus.service;

import com.ucampus.dto.HelpRequestDto;
import com.ucampus.dto.HelpResponseDto;
import com.ucampus.entity.Help;
import com.ucampus.repository.HelpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpServiceImpl implements HelpService {

    private final HelpRepository helpRepository;

    @Override
    public HelpResponseDto createHelp(HelpRequestDto requestDto) {
        // 如果content为空但description不为空，用description作为content
        String content = requestDto.getContent();
        if (content == null || content.trim().isEmpty()) {
            content = requestDto.getDescription();
        }

        Help help = Help.builder()
                .title(requestDto.getTitle())
                .content(content)
                .description(requestDto.getDescription())
                .author(requestDto.getAuthor())
                .contact(requestDto.getContact())
                .status(requestDto.getStatus() != null ? requestDto.getStatus() : "PENDING")
                .urgent(requestDto.getUrgent() != null ? requestDto.getUrgent() : false)
                .viewCount(0)
                .replyCount(0)
                .category(requestDto.getCategory())
                .location(requestDto.getLocation())
                .urgency(requestDto.getUrgency())
                .reward(requestDto.getReward())
                .publisherId(requestDto.getPublisherId())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        Help savedHelp = helpRepository.save(help);

        return toDto(savedHelp);
    }

    @Override
    public List<HelpResponseDto> getAllHelps() {
        return helpRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public HelpResponseDto getHelpById(Long id) {
        Help help = helpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Help not found: " + id));

        // 增加查看次数
        help.setViewCount(help.getViewCount() + 1);
        helpRepository.save(help);

        return toDto(help);
    }

    @Override
    public void deleteHelp(Long id) {
        if (!helpRepository.existsById(id)) {
            throw new RuntimeException("Help not found: " + id);
        }
        helpRepository.deleteById(id);
    }

    @Override
    public HelpResponseDto updateHelp(Long id, HelpRequestDto requestDto) {
        Help help = helpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Help not found: " + id));

        // 更新字段
        if (requestDto.getTitle() != null) {
            help.setTitle(requestDto.getTitle());
        }
        if (requestDto.getContent() != null) {
            help.setContent(requestDto.getContent());
        }
        if (requestDto.getDescription() != null) {
            help.setDescription(requestDto.getDescription());
        }
        if (requestDto.getAuthor() != null) {
            help.setAuthor(requestDto.getAuthor());
        }
        if (requestDto.getContact() != null) {
            help.setContact(requestDto.getContact());
        }
        if (requestDto.getStatus() != null) {
            help.setStatus(requestDto.getStatus());
        }
        if (requestDto.getUrgent() != null) {
            help.setUrgent(requestDto.getUrgent());
        }
        if (requestDto.getCategory() != null) {
            help.setCategory(requestDto.getCategory());
        }
        if (requestDto.getLocation() != null) {
            help.setLocation(requestDto.getLocation());
        }
        if (requestDto.getUrgency() != null) {
            help.setUrgency(requestDto.getUrgency());
        }
        if (requestDto.getReward() != null) {
            help.setReward(requestDto.getReward());
        }
        if (requestDto.getPublisherId() != null) {
            help.setPublisherId(requestDto.getPublisherId());
        }

        Help updatedHelp = helpRepository.save(help);
        return toDto(updatedHelp);
    }

    @Override
    public List<HelpResponseDto> getHelpsByAuthor(String author) {
        return helpRepository.findByAuthor(author)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<HelpResponseDto> getUrgentHelps() {
        return helpRepository.findByUrgentTrue()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private HelpResponseDto toDto(Help help) {
        return HelpResponseDto.builder()
                .id(help.getId())
                .title(help.getTitle())
                .content(help.getContent())
                .description(help.getDescription())
                .author(help.getAuthor())
                .contact(help.getContact())
                .status(help.getStatus())
                .urgent(help.getUrgent())
                .viewCount(help.getViewCount())
                .replyCount(help.getReplyCount())
                .createdAt(help.getCreatedAt())
                .category(help.getCategory())
                .location(help.getLocation())
                .urgency(help.getUrgency())
                .reward(help.getReward())
                .publisherId(help.getPublisherId())
                .build();
    }
}