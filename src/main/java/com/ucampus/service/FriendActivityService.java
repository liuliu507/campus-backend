package com.ucampus.service;

import com.ucampus.dto.FriendActivityDto;
import com.ucampus.entity.FriendActivity;
import com.ucampus.entity.FriendJoin;
import com.ucampus.repository.FriendActivityRepository;
import com.ucampus.repository.FriendJoinRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendActivityService {

    private final FriendActivityRepository activityRepository;
    private final FriendJoinRepository joinRepository;

    public FriendActivityService(FriendActivityRepository activityRepository, FriendJoinRepository joinRepository) {
        this.activityRepository = activityRepository;
        this.joinRepository = joinRepository;
    }

    public FriendActivity createActivity(FriendActivityDto dto) {
        FriendActivity a = new FriendActivity();
        a.setTitle(dto.title);
        a.setType(dto.type);
        a.setActivity(dto.activity);
        a.setLocation(dto.location);
        a.setTimeText(dto.timeText);
        a.setPeople(dto.people);
        a.setMaxParticipants(dto.maxParticipants != null ? dto.maxParticipants : 0);
        a.setDescription(dto.description);
        a.setContact(dto.contact);
        a.setUrgency(dto.urgency != null ? dto.urgency : "一般");
        a.setGender(dto.gender != null ? dto.gender : "不限");
        a.setTags(dto.tags != null ? dto.tags : List.of());
        a.setParticipants(0);
        return activityRepository.save(a);
    }

    public List<FriendActivity> listAll() {
        return activityRepository.findAll();
    }

    public Optional<FriendActivity> getById(Long id) {
        return activityRepository.findById(id);
    }

    @Transactional
    public FriendJoin joinActivity(Long activityId, String participantName, String participantContact) {
        FriendActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));

        // 检查是否已满（maxParticipants == 0 表示不限）
        if (activity.getMaxParticipants() != null && activity.getMaxParticipants() > 0) {
            if (activity.getParticipants() >= activity.getMaxParticipants()) {
                throw new RuntimeException("活动已满员");
            }
        }

        // 创建 join 记录
        FriendJoin join = new FriendJoin();
        join.setActivityId(activityId);
        join.setParticipantName(participantName);
        join.setParticipantContact(participantContact);
        join.setStatus("pending");
        FriendJoin saved = joinRepository.save(join);

        // 增加 participants 计数
        activity.setParticipants(activity.getParticipants() + 1);
        activityRepository.save(activity);

        return saved;
    }

    @Transactional
    public void leaveActivity(Long joinId) {
        FriendJoin join = joinRepository.findById(joinId)
                .orElseThrow(() -> new RuntimeException("报名记录不存在"));
        Long activityId = join.getActivityId();
        // 删除报名记录
        joinRepository.delete(join);

        // participants -1（不低于0）
        FriendActivity activity = activityRepository.findById(activityId).orElseThrow();
        int p = activity.getParticipants() == null ? 0 : activity.getParticipants();
        activity.setParticipants(Math.max(0, p - 1));
        activityRepository.save(activity);
    }

    public List<FriendJoin> listJoinsForActivity(Long activityId) {
        return joinRepository.findByActivityId(activityId);
    }
}
