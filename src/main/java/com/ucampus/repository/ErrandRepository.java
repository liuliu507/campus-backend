// src/main/java/com/ucampus/repository/ErrandRepository.java
package com.ucampus.repository;

import com.ucampus.entity.Errand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrandRepository extends JpaRepository<Errand, Long> {
    List<Errand> findByStatusOrderByCreatedAtDesc(String status);

    List<Errand> findByCategoryOrderByCreatedAtDesc(String category);

    @Query("SELECT e FROM Errand e WHERE " +
            "LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.fromLocation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Errand> searchByKeyword(@Param("keyword") String keyword);

    List<Errand> findByPublisherIdOrderByCreatedAtDesc(Long publisherId);

    // 检查任务是否存在
    boolean existsById(Long id);
}