package com.kintsugi.worker;

import com.kintsugi.worker.dto.JobDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class JobPoller {
    private static final Logger log = LoggerFactory.getLogger(JobPoller.class);

    private final CoordinatorClient coordinatorClient;
    private final JobExecutor jobExecutor;
    private final String workerId;

    //Thread pool allows the worker to run multiple jobs concurrently
    private final ExecutorService executor = Executors.newFixedThreadPool(4);


    public JobPoller(CoordinatorClient coordinatorClient, JobExecutor jobExecutor,
                     @Value("${app.worker-id:worker-1}") String workerId) {
        this.coordinatorClient = coordinatorClient;
        this.jobExecutor = jobExecutor;
        this.workerId = workerId;
    }

    @Scheduled(fixedDelay = 5000) // Runs every 5 seconds
    public void poll() {
        try {
            log.info("Pinging for jobs...");
            JobDto job = coordinatorClient.claimJob(workerId);

            if(job != null) {
                log.info("Claimed job {}. Submitting to thread pool...", job.getId());
                // Run the job in a background thread so the poller can keep polling
                executor.submit(() -> jobExecutor.execute(job, workerId));
            } else {
                log.info("No jobs available.");
            }
        } catch (Exception e) {
            log.error("Error during polling: {}", e.getMessage());
        }
    }
}
