package com.kintsugi.coordinator.job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    // wave 1: naive read-then-save claim goes here.
    // wave 3: replaced with the FOR UPDATE SKIP LOCKED native query.
}

