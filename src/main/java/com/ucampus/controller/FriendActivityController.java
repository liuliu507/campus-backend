package com.ucampus.controller;

import com.ucampus.dto.FriendActivityDto;
import com.ucampus.dto.FriendActivityResponseDto;
import com.ucampus.entity.FriendActivity;
import com.ucampus.entity.FriendJoin;
import com.ucampus.service.FriendActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class FriendActivityController {

    private final FriendActivityService service;

    public FriendActivityController(FriendActivityService service) {
        this.service = service;
    }

    // 创建活动
    @PostMapping("/create")
    public FriendActivityResponseDto create(@RequestBody FriendActivityDto dto) {
        FriendActivity a = service.createActivity(dto);
        return toDto(a);
    }

    // 列表（可以后续加分页/筛选）
    @GetMapping
    public List<FriendActivityResponseDto> listAll() {
        return service.listAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // 详情
    @GetMapping("/{id}")
    public FriendActivityResponseDto get(@PathVariable Long id) {
        FriendActivity a = service.getById(id).orElseThrow(() -> new RuntimeException("活动不存在"));
        return toDto(a);
    }

    // 加入活动（前端可以传 participantName 与 participantContact）
    @PostMapping("/{id}/join")
    public FriendJoin join(@PathVariable Long id,
                           @RequestParam(required = false) String participantName,
                           @RequestParam(required = false) String participantContact) {
        return service.joinActivity(id, participantName, participantContact);
    }

    // 删除活动 - 新增的删除接口
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteActivity(id);
        return "活动删除成功";
    }

    // 退出（传 joinId）
    @DeleteMapping("/join/{joinId}")
    public String leave(@PathVariable Long joinId) {
        service.leaveActivity(joinId);
        return "ok";
    }

    // 列出某活动的报名记录
    @GetMapping("/{id}/joins")
    public List<FriendJoin> joins(@PathVariable Long id) {
        return service.listJoinsForActivity(id);
    }

    private FriendActivityResponseDto toDto(FriendActivity a) {
        FriendActivityResponseDto d = new FriendActivityResponseDto();
        d.id = a.getId();
        d.title = a.getTitle();
        d.type = a.getType();
        d.activity = a.getActivity();
        d.location = a.getLocation();
        d.timeText = a.getTimeText();
        d.people = a.getPeople();
        d.maxParticipants = a.getMaxParticipants();
        d.description = a.getDescription();
        d.contact = a.getContact();
        d.urgency = a.getUrgency();
        d.gender = a.getGender();
        d.tags = a.getTags();
        d.participants = a.getParticipants();
        d.createdAt = a.getCreatedAt();
        return d;
    }
}