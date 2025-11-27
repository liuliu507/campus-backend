package com.ucampus.repository;

import com.ucampus.entity.FriendJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendJoinRepository extends JpaRepository<FriendJoin, Long> {
    List<FriendJoin> findByActivityId(Long activityId);
}
