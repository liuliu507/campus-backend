package com.ucampus.repository;

import com.ucampus.entity.FriendActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendActivityRepository extends JpaRepository<FriendActivity, Long> {
    List<FriendActivity> findByType(String type);
}
