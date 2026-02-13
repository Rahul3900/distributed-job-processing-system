package com.global.platform.job_processing_system.Controller;

import com.global.platform.job_processing_system.Enum.JobStatus;
import com.global.platform.job_processing_system.Repository.JobRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumMap;
import java.util.Map;

@RestController
@RequestMapping("/monitoring")
public class MonitoringController {

    private final JobRepository jobRepository;

    public MonitoringController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @GetMapping("/stats")
    public Map<JobStatus, Long> jobStats() {
        Map<JobStatus, Long> stats = new EnumMap<>(JobStatus.class);
        for (JobStatus status : JobStatus.values()) {
            stats.put(status, jobRepository.countByStatus(status));
        }
        return stats;
    }
}