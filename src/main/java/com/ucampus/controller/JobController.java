package com.ucampus.controller;

import com.ucampus.dto.ApiResponse;
import com.ucampus.dto.CreateJobRequest;
import com.ucampus.dto.JobDTO;
import com.ucampus.entity.Job;
import com.ucampus.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobDTO>>> getAllJobs() {
        try {
            List<JobDTO> jobs = jobService.getAllJobs();
            return ResponseEntity.ok(ApiResponse.success("获取职位列表成功", jobs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("获取职位列表失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(@PathVariable Long id) {
        try {
            JobDTO job = jobService.getJobById(id);
            return ResponseEntity.ok(ApiResponse.success("获取职位详情成功", job));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("获取职位详情失败: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobDTO>> createJob(@RequestBody CreateJobRequest request) {
        try {
            String publisherId = "publisher_" + System.currentTimeMillis();
            String publisherName = "匿名发布者";

            JobDTO job = jobService.createJob(request, publisherId, publisherName);
            return ResponseEntity.ok(ApiResponse.success("职位发布成功", job));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("职位发布失败: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<JobDTO>>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String category) {
        try {
            List<JobDTO> jobs = jobService.searchJobs(keyword, jobType, category);
            return ResponseEntity.ok(ApiResponse.success("搜索成功", jobs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("搜索失败: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<ApiResponse<Void>> applyJob(@PathVariable Long id) {
        try {
            jobService.applyJob(id);
            return ResponseEntity.ok(ApiResponse.success("申请成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("申请失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<JobDTO>> updateJobStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            Job.JobStatus jobStatus = Job.JobStatus.valueOf(status.toUpperCase());
            JobDTO job = jobService.updateJobStatus(id, jobStatus);
            return ResponseEntity.ok(ApiResponse.success("状态更新成功", job));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("状态更新失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJob(id);
            return ResponseEntity.ok(ApiResponse.success("职位删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("职位删除失败: " + e.getMessage()));
        }
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<ApiResponse<List<JobDTO>>> getMyJobs() {
        try {
            List<JobDTO> jobs = jobService.getAllJobs();
            return ResponseEntity.ok(ApiResponse.success("获取我的职位成功", jobs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("获取我的职位失败: " + e.getMessage()));
        }
    }

    @GetMapping("/job-types")
    public ResponseEntity<ApiResponse<List<String>>> getJobTypes() {
        try {
            List<String> jobTypes = List.of("全部", "兼职", "实习", "全职");
            return ResponseEntity.ok(ApiResponse.success("获取工作类型成功", jobTypes));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("获取工作类型失败: " + e.getMessage()));
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getCategories() {
        try {
            List<String> categories = List.of(
                    "全部", "技术开发", "市场推广", "教育辅导", "行政文员",
                    "设计创意", "餐饮服务", "销售业务", "其他"
            );
            return ResponseEntity.ok(ApiResponse.success("获取分类成功", categories));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("获取分类失败: " + e.getMessage()));
        }
    }
}