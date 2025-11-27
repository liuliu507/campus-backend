// src/main/java/com/ucampus/controller/ErrandController.java
package com.ucampus.controller;

import com.ucampus.dto.ErrandDTO;
import com.ucampus.dto.CreateErrandRequest;
import com.ucampus.service.ErrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // ==================== 删除跑腿任务 ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteErrand(@PathVariable Long id) {
        System.out.println("🔄 删除跑腿任务 ID=" + id);

        try {
            errandService.deleteErrand(id);
            System.out.println("✅ 删除成功");
            return ResponseEntity.ok(success("删除成功"));
        } catch (Exception e) {
            System.err.println("❌ 删除失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(error("删除失败：" + e.getMessage()));
        }
    }

    // ====== 工具：统一的错误格式 ======
    private Map<String, Object> error(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("message", msg);
        return map;
    }

    private Map<String, Object> success(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("message", msg);
        return map;
    }
}