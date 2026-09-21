package com.kintsugi.worker;

import com.kintsugi.worker.dto.JobDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobExecutor {
    private static final Logger log = LoggerFactory.getLogger(JobExecutor.class);
    private final CoordinatorClient coordinatorClient;

    public JobExecutor(CoordinatorClient coordinatorClient) {
        this.coordinatorClient = coordinatorClient;
    }

    public void execute(JobDto job, String workerId) {
        try {
            log.info("Executing job {} [Type: {}]...", job.getId(), job.getType());

            // Simulate real work
            Thread.sleep(2000);

            log.info("Job {} completed successfully!", job.getId());

            // Tell the coordinator we are done
            coordinatorClient.acknowledgeJob(job.getId(), workerId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Job {} was interrupted", job.getId());
        } catch (Exception e) {
            log.error("Error executing job {}: {}", job.getId(), e.getMessage());
        }
    }
}
