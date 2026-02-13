package com.global.platform.job_processing_system.Service;

import com.global.platform.job_processing_system.Entity.Job;
import com.global.platform.job_processing_system.Enum.JobStatus;
import com.global.platform.job_processing_system.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Job createJob(String jobName) {
        Job job = new Job();
        job.setJobName(jobName);
        job.setStatus(JobStatus.CREATED);
        job.setRetryCount(0);
        job.setMaxRetries(3);
        job.setCreatedAt(LocalDateTime.now());

        Job savedJob = jobRepository.save(job);
        return savedJob;
    }
}
