package com.ucampus.repository;

import com.ucampus.entity.Help;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {
    // 可以根据需要添加自定义查询方法
    List<Help> findByAuthor(String author);
    List<Help> findByStatus(String status);
    List<Help> findByUrgentTrue();
}