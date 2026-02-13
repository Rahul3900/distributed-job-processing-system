package com.global.platform.job_processing_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class JobProcessingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobProcessingSystemApplication.class, args);
	}

}
