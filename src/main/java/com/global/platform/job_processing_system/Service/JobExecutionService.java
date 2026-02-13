package com.global.platform.job_processing_system.Service;

import com.global.platform.job_processing_system.Entity.Job;
import com.global.platform.job_processing_system.Enum.JobStatus;
import com.global.platform.job_processing_system.Repository.JobRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@EnableAsync
public class JobExecutionService {

    private static final Logger log =
            LoggerFactory.getLogger(JobExecutionService.class);

    private final JobRepository jobRepository;

    public JobExecutionService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Async("taskExecutor")
    @Transactional
    public void executeJob(Long jobId) {

        Job job = jobRepository.findById(jobId).orElseThrow();

        long start = System.currentTimeMillis();

        try {
            job.setStartedAt(LocalDateTime.now());

            // simulate work
            Thread.sleep(200);

            job.setStatus(JobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());

        } catch (Exception ex) {
            job.setStatus(JobStatus.FAILED);
        } finally {
            job.setExecutionTimeMs(System.currentTimeMillis() - start);
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);   // 🔴 MUST BE HERE
        }
    }

}
