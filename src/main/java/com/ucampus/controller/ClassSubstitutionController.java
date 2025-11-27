// src/main/java/com/ucampus/controller/ClassSubstitutionController.java
package com.ucampus.controller;

import com.ucampus.dto.ClassSubstitutionDTO;
import com.ucampus.dto.CreateSubstitutionRequest;
import com.ucampus.service.ClassSubstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-substitution")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ClassSubstitutionController {

    private final ClassSubstitutionService substitutionService;

    @GetMapping
    public ResponseEntity<List<ClassSubstitutionDTO>> getAllSubstitutions() {
        List<ClassSubstitutionDTO> substitutions = substitutionService.getAllSubstitutions();
        return ResponseEntity.ok(substitutions);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ClassSubstitutionDTO>> getPendingSubstitutions() {
        List<ClassSubstitutionDTO> substitutions = substitutionService.getPendingSubstitutions();
        return ResponseEntity.ok(substitutions);
    }

    @PostMapping
    public ResponseEntity<ClassSubstitutionDTO> createSubstitution(@RequestBody CreateSubstitutionRequest request) {
        ClassSubstitutionDTO created = substitutionService.createSubstitution(request);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<ClassSubstitutionDTO> acceptSubstitution(
            @PathVariable Long id,
            @RequestParam Long acceptorId) {
        ClassSubstitutionDTO updated = substitutionService.acceptSubstitution(id, acceptorId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeSubstitution(@PathVariable Long id) {
        substitutionService.likeSubstitution(id);
        return ResponseEntity.ok().build();
    }
}