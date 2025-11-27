package com.ucampus.controller;

import com.ucampus.dto.ShareDto;
import com.ucampus.entity.Share;
import com.ucampus.service.ShareService;
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
}
