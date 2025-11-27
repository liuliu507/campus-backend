package com.ucampus.repository;

import com.ucampus.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByStatus(String status);
    List<Rating> findByTargetIdAndTargetType(Long targetId, String targetType);
}
