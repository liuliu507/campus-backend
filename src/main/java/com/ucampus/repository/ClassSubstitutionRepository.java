// src/main/java/com/ucampus/repository/ClassSubstitutionRepository.java
package com.ucampus.repository;

import com.ucampus.entity.ClassSubstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassSubstitutionRepository extends JpaRepository<ClassSubstitution, Long> {

    List<ClassSubstitution> findByStatusOrderByCreatedAtDesc(String status);

    List<ClassSubstitution> findByCampusAndCourseTypeOrderByCreatedAtDesc(String campus, String courseType);

    @Query("SELECT cs FROM ClassSubstitution cs WHERE " +
            "LOWER(cs.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(cs.courseName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(cs.teacher) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ClassSubstitution> searchByKeyword(@Param("keyword") String keyword);

    List<ClassSubstitution> findByPublisherIdOrderByCreatedAtDesc(Long publisherId);
}