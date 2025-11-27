// src/main/java/com/ucampus/controller/ErrandController.java
package com.ucampus.controller;

import com.ucampus.dto.ErrandDTO;
import com.ucampus.dto.CreateErrandRequest;
import com.ucampus.service.ErrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/errands")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ErrandController {

    private final ErrandService errandService;

    @GetMapping
    public ResponseEntity<List<ErrandDTO>> getAllErrands() {
        List<ErrandDTO> errands = errandService.getAllErrands();
        return ResponseEntity.ok(errands);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ErrandDTO>> getPendingErrands() {
        List<ErrandDTO> errands = errandService.getPendingErrands();
        return ResponseEntity.ok(errands);
    }

    @PostMapping
    public ResponseEntity<ErrandDTO> createErrand(@RequestBody CreateErrandRequest request) {
        ErrandDTO created = errandService.createErrand(request);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<ErrandDTO> acceptErrand(
            @PathVariable Long id,
            @RequestParam Long acceptorId) {
        ErrandDTO updated = errandService.acceptErrand(id, acceptorId);
        return ResponseEntity.ok(updated);
    }
}