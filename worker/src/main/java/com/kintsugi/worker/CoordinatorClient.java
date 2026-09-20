package com.kintsugi.worker;

import org.springframework.web.client.RestTemplate;

public class CoordinatorClient {
    private final RestTemplate restTemplate;
    private final String coordinatorUrl;

    public CoordinatorClient(RestTemplate restTemplate,
                             @Value("${app.coordinator-url:httpl://coordinator:8080}") String
                             coordinatorUrl) {
        this.restTemplate = restTemplate;
        this.coordinatorUrl = coordinatorUrl;
    }

    public String ping() {
        // This exists by default in Spring Boot
        return restTemplate.getForObject(coordinatorUrl + "/actuator/health", String.class);
    }
}
