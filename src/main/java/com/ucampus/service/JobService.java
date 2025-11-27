package com.ucampus.service;

import com.ucampus.dto.CreateJobRequest;
import com.ucampus.dto.JobDTO;
import com.ucampus.entity.Job;
import com.ucampus.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final ObjectMapper objectMapper;

    // 获取所有职位
    public List<JobDTO> getAllJobs() {
        List<Job> jobs = jobRepository.findByStatusOrderByCreatedAtDesc(Job.JobStatus.OPEN);

        if (jobs.isEmpty()) {
            return getMockJobs(); // 如果没有数据，返回模拟数据
        }

        return jobs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 根据ID获取职位
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElse(null);

        if (job == null) {
            // 如果数据库中没有，从模拟数据中查找
            return getMockJobs().stream()
                    .filter(j -> j.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("职位不存在"));
        }

        // 增加浏览量
        jobRepository.incrementViewCount(id);

        return convertToDTO(job);
    }

    // 创建职位
    public JobDTO createJob(CreateJobRequest request, String publisherId, String publisherName) {
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setJobType(request.getJobType());
        job.setCategory(request.getCategory());
        job.setSalary(request.getSalary());
        job.setLocation(request.getLocation());
        job.setWorkAddress(request.getWorkAddress());
        job.setContactInfo(request.getContactInfo());
        job.setContactPerson(request.getContactPerson());
        job.setRequirements(request.getRequirements());
        job.setBenefits(request.getBenefits());
        job.setWorkHours(request.getWorkHours());
        job.setPublisherId(publisherId);
        job.setPublisherName(publisherName);
        job.setUrgent(request.getUrgent() != null ? request.getUrgent() : false);
        job.setStatus(Job.JobStatus.OPEN);
        job.setViewCount(0);
        job.setApplyCount(0);

        // 处理图片
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            try {
                job.setImageUrls(objectMapper.writeValueAsString(request.getImages()));
            } catch (JsonProcessingException e) {
                job.setImageUrls("[\"💼\"]");
            }
        } else {
            job.setImageUrls("[\"💼\"]");
        }

        Job savedJob = jobRepository.save(job);
        return convertToDTO(savedJob);
    }

    // 搜索职位
    public List<JobDTO> searchJobs(String keyword, String jobType, String category) {
        List<Job> jobs;

        if (keyword != null && !keyword.trim().isEmpty()) {
            jobs = jobRepository.searchByKeyword(keyword.trim(), Job.JobStatus.OPEN);
        } else if (jobType != null && !"全部".equals(jobType)) {
            jobs = jobRepository.findByJobTypeAndStatusOrderByCreatedAtDesc(jobType, Job.JobStatus.OPEN);
        } else if (category != null && !"全部".equals(category)) {
            jobs = jobRepository.findByCategoryAndStatusOrderByCreatedAtDesc(category, Job.JobStatus.OPEN);
        } else {
            jobs = jobRepository.findByStatusOrderByCreatedAtDesc(Job.JobStatus.OPEN);
        }

        if (jobs.isEmpty()) {
            return searchMockJobs(keyword, jobType, category);
        }

        return jobs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 申请职位
    public void applyJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("职位不存在"));

        // 增加申请量
        jobRepository.incrementApplyCount(jobId);
    }

    // 更新职位状态
    public JobDTO updateJobStatus(Long id, Job.JobStatus status) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("职位不存在"));

        job.setStatus(status);
        Job updatedJob = jobRepository.save(job);
        return convertToDTO(updatedJob);
    }

    // 删除职位
    public void deleteJob(Long id, String publisherId) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("职位不存在"));

        if (!job.getPublisherId().equals(publisherId)) {
            throw new RuntimeException("无权删除此职位");
        }

        jobRepository.delete(job);
    }

    // 实体转DTO
    private JobDTO convertToDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setCompany(job.getCompany());
        dto.setJobType(job.getJobType());
        dto.setCategory(job.getCategory());
        dto.setSalary(job.getSalary());
        dto.setLocation(job.getLocation());
        dto.setWorkAddress(job.getWorkAddress());
        dto.setContactInfo(job.getContactInfo());
        dto.setContactPerson(job.getContactPerson());
        dto.setRequirements(job.getRequirements());
        dto.setBenefits(job.getBenefits());
        dto.setWorkHours(job.getWorkHours());
        dto.setPublisherId(job.getPublisherId());
        dto.setPublisherName(job.getPublisherName());
        dto.setUrgent(job.getUrgent());
        dto.setStatus(job.getStatus().name());
        dto.setViewCount(job.getViewCount());
        dto.setApplyCount(job.getApplyCount());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setExpireDate(job.getExpireDate());
        dto.setTimeAgo(dto.calculateTimeAgo());
        dto.setDaysLeft(dto.calculateDaysLeft());

        // 处理图片
        if (job.getImageUrls() != null && !job.getImageUrls().isEmpty()) {
            try {
                List<String> images = objectMapper.readValue(job.getImageUrls(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
                dto.setImages(images);
            } catch (JsonProcessingException e) {
                dto.setImages(List.of("💼"));
            }
        } else {
            dto.setImages(List.of("💼"));
        }

        return dto;
    }

    // 模拟数据（用于测试）
    private List<JobDTO> getMockJobs() {
        List<JobDTO> jobs = new ArrayList<>();

        // 添加模拟职位数据
        jobs.add(createMockJob(1L, "校园推广专员", "负责校园产品推广", "某科技公司", "兼职",
                "市场推广", "2000-3000元/月", "主校区", true));

        jobs.add(createMockJob(2L, "软件开发实习生", "参与公司产品开发", "某软件公司", "实习",
                "技术开发", "3000-5000元/月", "全市", false));

        jobs.add(createMockJob(3L, "家教老师", "辅导初中数学", "个人", "兼职",
                "教育辅导", "100元/小时", "东校区", false));

        return jobs;
    }

    private JobDTO createMockJob(Long id, String title, String description, String company,
                                 String jobType, String category, String salary, String location, Boolean urgent) {
        JobDTO job = new JobDTO();
        job.setId(id);
        job.setTitle(title);
        job.setDescription(description);
        job.setCompany(company);
        job.setJobType(jobType);
        job.setCategory(category);
        job.setSalary(salary);
        job.setLocation(location);
        job.setWorkAddress(location + "具体面议");
        job.setContactInfo("138****1234");
        job.setContactPerson("张经理");
        job.setRequirements("有相关经验者优先");
        job.setBenefits("提供培训，表现优秀者可转正");
        job.setWorkHours("周一至周五，弹性工作");
        job.setPublisherId("publisher" + id);
        job.setPublisherName("发布者" + id);
        job.setUrgent(urgent);
        job.setStatus("OPEN");
        job.setViewCount((int)(Math.random() * 100) + 20);
        job.setApplyCount((int)(Math.random() * 30) + 5);
        job.setCreatedAt(LocalDateTime.now().minusHours(id * 8));
        job.setExpireDate(LocalDateTime.now().plusDays(30 - id * 5));
        job.setTimeAgo(job.calculateTimeAgo());
        job.setDaysLeft(job.calculateDaysLeft());
        job.setImages(List.of("💼"));

        return job;
    }

    // 搜索模拟数据
    private List<JobDTO> searchMockJobs(String keyword, String jobType, String category) {
        List<JobDTO> allJobs = getMockJobs();

        return allJobs.stream()
                .filter(job -> {
                    boolean matchesKeyword = true;
                    boolean matchesJobType = true;
                    boolean matchesCategory = true;

                    if (keyword != null && !keyword.trim().isEmpty()) {
                        matchesKeyword = job.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                                job.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                                job.getCompany().toLowerCase().contains(keyword.toLowerCase());
                    }

                    if (jobType != null && !"全部".equals(jobType)) {
                        matchesJobType = job.getJobType().equals(jobType);
                    }

                    if (category != null && !"全部".equals(category)) {
                        matchesCategory = job.getCategory().equals(category);
                    }

                    return matchesKeyword && matchesJobType && matchesCategory;
                })
                .collect(Collectors.toList());
    }
}