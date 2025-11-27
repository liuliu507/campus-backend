package com.ucampus.controller;

import com.ucampus.dto.ShareDto;
import com.ucampus.entity.Share;
import com.ucampus.service.ShareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/share")
@CrossOrigin(origins = "http://localhost:3000")
public class ShareController {

    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    // 发布趣事
    @PostMapping("/create")
    public Share createShare(@RequestBody ShareDto dto) {
        return shareService.createShare(dto);
    }

    // 获取所有趣事
    @GetMapping("/all")
    public List<Share> getAllShares() {
        return shareService.getAllShares();
    }

    // 获取某个用户的趣事
    @GetMapping("/user/{userId}")
    public List<Share> getUserShares(@PathVariable Long userId) {
        return shareService.getSharesByUser(userId);
    }

    // 删除趣事
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShare(@PathVariable Long id) {
        try {
            shareService.deleteShare(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}