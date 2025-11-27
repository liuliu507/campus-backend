package com.ucampus.service;

import com.ucampus.dto.ShareDto;
import com.ucampus.entity.Share;
import com.ucampus.repository.ShareRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShareService {

    private final ShareRepository shareRepository;

    public ShareService(ShareRepository shareRepository) {
        this.shareRepository = shareRepository;
    }

    // 发布趣事
    public Share createShare(ShareDto dto) {
        Share share = new Share();
        share.setUserId(dto.getUserId());
        share.setContent(dto.getContent());
        share.setImageUrl(dto.getImageUrl());
        share.setCreatedAt(LocalDateTime.now());
        return shareRepository.save(share);
    }

    // 获取所有趣事
    public List<Share> getAllShares() {
        return shareRepository.findAll();
    }

    // 获取某个用户的趣事
    public List<Share> getSharesByUser(Long userId) {
        return shareRepository.findAllByUserId(userId);
    }

    // 删除趣事
    public void deleteShare(Long id) {
        if (!shareRepository.existsById(id)) {
            throw new RuntimeException("Share not found: " + id);
        }
        shareRepository.deleteById(id);
    }
}