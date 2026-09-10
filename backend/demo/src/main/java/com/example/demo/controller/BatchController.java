package com.example.demo.controller;

import java.util.Map;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchController {

	private final JobOperator jobOperator;
	private final Job importUserJob;
	
	@PostMapping("/import-users")
	public ResponseEntity<?> importUsers() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong("startTime", System.currentTimeMillis())
					.toJobParameters();
			
			JobExecution execution = jobOperator.start(importUserJob, jobParameters);
			
			return ResponseEntity.ok(Map.of(
					"status", execution.getStatus().toString(),
					"exitStatus", execution.getExitStatus().getExitCode(),
					"startTime", execution.getStartTime(),
					"endTime", execution.getEndTime()
					));
			
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
		}
	}
}
