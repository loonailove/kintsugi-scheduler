package com.kintsugi.coordinator.job;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> submit(@RequestBody JobSubmitRequest request) {
        Job job = Job.builder()
                .type(request.getType())
                .payload(request.getPayload())
                .priority(request.getPriority())
                .maxRetries(request.getMaxRetries())
                .status(JobStatus.PENDING)
                .retryCount(0)
                .createdAt(Instant.now())
                .build();

        Job saved = jobRepository.save(job);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", saved.getId()));
    }

    @PostMapping("/claim")
    public ResponseEntity<Job> claim(@RequestBody ClaimRequest request) {
        Optional<Job> next = jobRepository
                .findFirstByStatusOrderByPriorityDescCreatedAtAsc(JobStatus.PENDING);

        if (next.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Job job = next.get();
        job.setStatus(JobStatus.RUNNING);
        job.setAssignedWorkerId(request.getWorkerId());
        Job saved = jobRepository.save(job);

        return ResponseEntity.ok(saved);
    }
}