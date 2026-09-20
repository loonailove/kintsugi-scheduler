package com.kintsugi.worker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobPoller {
    private final CoordinatorClient coordinatorClient;

    public JobPoller(CoordinatorClient coordinatorClient) {
        this.coordinatorClient = coordinatorClient;
    }

    @Scheduled(fixedDelay = 5000) // Runs every 5 seconds
    public void poll() {
        try {
            log.info("Pinging coordinator...");
            String response = coordinatorClient.ping();
            log.info("Coordinator response: {}", response);
        } catch (Exception e) {
            log.error("Could not connect to coordinator: {}", e.getMessage());
        }
    }
}
