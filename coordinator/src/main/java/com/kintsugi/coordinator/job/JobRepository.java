package com.kintsugi.coordinator.job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    // Wave 1: naive read-then-save claim. Reads the highest-priority,
    // oldest PENDING job.
    Optional<Job> findFirstByStatusOrderByPriorityDescCreatedAtAsc(JobStatus status);

    // wave 3: replaced with the FOR UPDATE SKIP LOCKED native query.
}

