package com.ucampus.service;

import com.ucampus.dto.HelpRequestDto;
import com.ucampus.dto.HelpResponseDto;

import java.util.List;

public interface HelpService {
    HelpResponseDto createHelp(HelpRequestDto requestDto);
    List<HelpResponseDto> getAllHelps();
    HelpResponseDto getHelpById(Long id);
    void deleteHelp(Long id);
    HelpResponseDto updateHelp(Long id, HelpRequestDto requestDto);
    List<HelpResponseDto> getHelpsByAuthor(String author);
    List<HelpResponseDto> getUrgentHelps();
}