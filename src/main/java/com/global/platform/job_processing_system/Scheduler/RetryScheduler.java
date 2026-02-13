package com.global.platform.job_processing_system.Scheduler;

import com.global.platform.job_processing_system.Entity.Job;
import com.global.platform.job_processing_system.Enum.JobStatus;
import com.global.platform.job_processing_system.Repository.JobRepository;
import com.global.platform.job_processing_system.Service.JobExecutionService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RetryScheduler {

    private final JobRepository jobRepository;
    private final JobExecutionService executionService;

    public RetryScheduler(JobRepository jobRepository,
                          JobExecutionService executionService) {
        this.jobRepository = jobRepository;
        this.executionService = executionService;
    }
    @Transactional
    @Scheduled(fixedDelay = 10000)
    public void retryFailedJobs() {

        Page<Job> failedPage =
                jobRepository.findByStatusOrderByCreatedAtAsc(
                        JobStatus.FAILED,
                        PageRequest.of(0, 10)
                );

        List<Job> failedJobs = failedPage.getContent();

        for (Job job : failedJobs) {
            if (job.getRetryCount() < job.getMaxRetries()) {

                job.setRetryCount(job.getRetryCount() + 1);
                job.setStatus(JobStatus.RETRYING);
                job.setUpdatedAt(LocalDateTime.now());

                jobRepository.save(job);

                executionService.executeJob(job.getId());
            }
        }
    }

}