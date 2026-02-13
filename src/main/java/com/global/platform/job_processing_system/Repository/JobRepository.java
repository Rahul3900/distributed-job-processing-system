package com.global.platform.job_processing_system.Repository;

import com.global.platform.job_processing_system.Entity.Job;
import com.global.platform.job_processing_system.Enum.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface JobRepository extends JpaRepository<Job,Long> {

    // Used to enforce max RUNNING limit
    long countByStatus(JobStatus status);

    // Used by scheduler to pick CREATED jobs safely & dynamically
    Page<Job> findByStatusOrderByCreatedAtAsc(
            JobStatus status,
            Pageable pageable
    );
}
