package com.global.platform.job_processing_system.Controller;


import com.global.platform.job_processing_system.Entity.Job;
import com.global.platform.job_processing_system.Service.JobService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public Job createJob(@RequestParam String jobName) {
        return jobService.createJob(jobName);
    }
}
