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

import java.util.List;

@Service
public class JobExecutionScheduler {

    private final JobRepository jobRepository;
    private final JobExecutionService jobExecutionService;

    public JobExecutionScheduler(JobRepository jobRepository,
                                 JobExecutionService jobExecutionService) {
        this.jobRepository = jobRepository;
        this.jobExecutionService = jobExecutionService;
    }

    /**
     * Picks CREATED jobs and executes them with controlled concurrency.
     */
    private static final int MAX_CONCURRENT_JOBS = 10;

    @Scheduled(fixedDelay = 2000)
    public void pickAndExecuteJobs() {

        long runningCount =
                jobRepository.countByStatus(JobStatus.RUNNING);

        int capacity = MAX_CONCURRENT_JOBS - (int) runningCount;

        if (capacity <= 0) {
            return;
        }

        Page<Job> page =
                jobRepository.findByStatusOrderByCreatedAtAsc(
                        JobStatus.CREATED,
                        PageRequest.of(0, capacity)
                );

        List<Job> jobs = page.getContent();


        for (Job job : jobs) {
            job.setStatus(JobStatus.RUNNING);
            jobRepository.save(job);

            jobExecutionService.executeJob(job.getId());
        }
    }

}
