package com.ucampus.controller;

import com.ucampus.dto.HelpRequestDto;
import com.ucampus.dto.HelpResponseDto;
import com.ucampus.service.HelpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/help")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HelpController {

    private final HelpService helpService;

    @PostMapping
    public ResponseEntity<HelpResponseDto> createHelp(@RequestBody HelpRequestDto dto) {
        try {
            HelpResponseDto response = helpService.createHelp(dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<HelpResponseDto>> getAllHelps() {
        try {
            List<HelpResponseDto> helps = helpService.getAllHelps();
            return ResponseEntity.ok(helps);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HelpResponseDto> getHelpById(@PathVariable Long id) {
        try {
            HelpResponseDto help = helpService.getHelpById(id);
            return ResponseEntity.ok(help);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HelpResponseDto> updateHelp(@PathVariable Long id, @RequestBody HelpRequestDto dto) {
        try {
            HelpResponseDto response = helpService.updateHelp(id, dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHelp(@PathVariable Long id) {
        try {
            helpService.deleteHelp(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<HelpResponseDto>> getHelpsByAuthor(@PathVariable String author) {
        try {
            List<HelpResponseDto> helps = helpService.getHelpsByAuthor(author);
            return ResponseEntity.ok(helps);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/urgent")
    public ResponseEntity<List<HelpResponseDto>> getUrgentHelps() {
        try {
            List<HelpResponseDto> helps = helpService.getUrgentHelps();
            return ResponseEntity.ok(helps);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}