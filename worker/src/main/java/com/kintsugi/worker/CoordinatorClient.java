package com.kintsugi.worker;

import com.kintsugi.worker.dto.ClaimRequest;
import com.kintsugi.worker.dto.JobDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Locale;

@Component
public class CoordinatorClient {
    private final RestTemplate restTemplate;
    private final String coordinatorUrl;

    public CoordinatorClient(RestTemplate restTemplate,
                             @Value("${app.coordinator-url:http://coordinator:8080}") String
                             coordinatorUrl) {
        this.restTemplate = restTemplate;
        this.coordinatorUrl = coordinatorUrl;
    }

    public JobDto claimJob(String workerId) {
        ClaimRequest request = new ClaimRequest(workerId);
        ResponseEntity<JobDto> response = restTemplate.postForEntity(
                coordinatorUrl + "/jobs/claim",
                request,
                JobDto.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return null;
    }

    public void acknowledgeJob(java.util.UUID jobId, String workerId) {
        // Empty body for the ACK request
        restTemplate.postForEntity(
                coordinatorUrl + "/jobs/" + jobId + "/ack-success",
                new ClaimRequest(workerId),
                Void.class
            );
        }
    }

