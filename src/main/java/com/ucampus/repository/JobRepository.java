package com.ucampus.repository;

import com.ucampus.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    // 根据工作类型和状态查找
    List<Job> findByJobTypeAndStatusOrderByCreatedAtDesc(String jobType, Job.JobStatus status);

    // 根据分类和状态查找
    List<Job> findByCategoryAndStatusOrderByCreatedAtDesc(String category, Job.JobStatus status);

    // 根据发布者ID查找
    List<Job> findByPublisherIdOrderByCreatedAtDesc(String publisherId);

    // 搜索职位
    @Query("SELECT j FROM Job j WHERE " +
            "(LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "j.status = :status " +
            "ORDER BY j.urgent DESC, j.createdAt DESC")
    List<Job> searchByKeyword(@Param("keyword") String keyword,
                              @Param("status") Job.JobStatus status);

    // 查找急招职位
    List<Job> findByUrgentAndStatusOrderByCreatedAtDesc(Boolean urgent, Job.JobStatus status);

    // 增加浏览量
    @Modifying
    @Query("UPDATE Job j SET j.viewCount = j.viewCount + 1 WHERE j.id = :id")
    void incrementViewCount(@Param("id") Long id);

    // 增加申请量
    @Modifying
    @Query("UPDATE Job j SET j.applyCount = j.applyCount + 1 WHERE j.id = :id")
    void incrementApplyCount(@Param("id") Long id);

    // 根据状态查找职位
    List<Job> findByStatusOrderByCreatedAtDesc(Job.JobStatus status);

    // 查找即将过期的职位
    @Query("SELECT j FROM Job j WHERE j.expireDate < :date AND j.status = 'OPEN'")
    List<Job> findExpiringJobs(@Param("date") LocalDateTime date);
}