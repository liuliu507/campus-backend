package com.ucampus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "friend_activity")
public class FriendActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type;         // 活动类型，例如：运动健身
    private String activity;     // 具体活动，例如：羽毛球
    private String location;
    private String timeText;     // 活动时间（文本，前端传来）
    private String people;       // 人数要求（文本）
    private Integer maxParticipants = 0; // 0 表示不限
    @Column(columnDefinition = "TEXT")
    private String description;
    private String contact;      // 发布者联系方式

    private String urgency = "一般";
    private String gender = "不限";

    @ElementCollection
    @CollectionTable(name = "friend_activity_tags", joinColumns = @JoinColumn(name = "activity_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    private Integer participants = 0; // 当前已加入人数

    private LocalDateTime createdAt = LocalDateTime.now();

    // --- getters / setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getTimeText() { return timeText; }
    public void setTimeText(String timeText) { this.timeText = timeText; }

    public String getPeople() { return people; }
    public void setPeople(String people) { this.people = people; }

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Integer getParticipants() { return participants; }
    public void setParticipants(Integer participants) { this.participants = participants; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
