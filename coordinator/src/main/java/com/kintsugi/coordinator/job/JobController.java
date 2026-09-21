package com.kintsugi.coordinator.job;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);

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

    @PostMapping("/{id}/ack-success")
    public ResponseEntity<Void> acknowledgeSuccess(@PathVariable UUID id,
                                                   @RequestBody ClaimRequest request) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));

        // Ownership validation
        if (job.getAssignedWorkerId() == null ||
        !job.getAssignedWorkerId().equals(request.getWorkerId())) {
            log.error("Unauthorized attempt to acknowledge job {} by worker {}", id, request.getWorkerId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        }

        if (job.getStatus() != JobStatus.RUNNING) {
            log.error("Job {} is in status {}, cannot mark as succeeded", id, job.getStatus());
            return ResponseEntity.badRequest().build(); // 400
        }

        job.setStatus(JobStatus.SUCCEEDED);
        jobRepository.save(job);

        return ResponseEntity.ok().build();
    }
}