// src/main/java/com/ucampus/controller/ClassSubstitutionController.java
package com.ucampus.controller;

import com.ucampus.dto.ClassSubstitutionDTO;
import com.ucampus.dto.CreateSubstitutionRequest;
import com.ucampus.service.ClassSubstitutionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/class-substitution")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 允许所有来源，便于调试
public class ClassSubstitutionController {

    private final ClassSubstitutionService substitutionService;
    private static final Logger logger = LoggerFactory.getLogger(ClassSubstitutionController.class);

    @GetMapping
    public ResponseEntity<List<ClassSubstitutionDTO>> getAllSubstitutions() {
        logger.info("接收到获取所有代课需求的请求");
        List<ClassSubstitutionDTO> substitutions = substitutionService.getAllSubstitutions();
        logger.info("返回 {} 个代课需求", substitutions.size());
        return ResponseEntity.ok(substitutions);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ClassSubstitutionDTO>> getPendingSubstitutions() {
        logger.info("接收到获取待处理代课需求的请求");
        List<ClassSubstitutionDTO> substitutions = substitutionService.getPendingSubstitutions();
        return ResponseEntity.ok(substitutions);
    }

    @PostMapping
    public ResponseEntity<?> createSubstitution(@RequestBody CreateSubstitutionRequest request) {
        logger.info("接收到创建代课需求的请求");
        logger.info("请求体: title={}, campus={}, publisherId={}",
                request.getTitle(), request.getCampus(), request.getPublisherId());

        try {
            ClassSubstitutionDTO created = substitutionService.createSubstitution(request);
            logger.info("代课需求创建成功，ID: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("创建代课需求时发生控制器级别错误", e);

            // 返回详细的错误信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", 500);
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("detailedMessage", e.toString());
            errorResponse.put("path", "/api/class-substitution");

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptSubstitution(
            @PathVariable Long id,
            @RequestParam Long acceptorId) {
        logger.info("接收到接单请求 - 代课ID: {}, 接单者ID: {}", id, acceptorId);

        try {
            ClassSubstitutionDTO updated = substitutionService.acceptSubstitution(id, acceptorId);
            logger.info("接单成功 - 代课ID: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("接单时发生错误 - 代课ID: {}", id, e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", 500);
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("detailedMessage", e.toString());
            errorResponse.put("path", "/api/class-substitution/" + id + "/accept");

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeSubstitution(@PathVariable Long id) {
        logger.info("接收到点赞请求 - 代课ID: {}", id);
        substitutionService.likeSubstitution(id);
        return ResponseEntity.ok().build();
    }
}