package com.kintsugi.worker;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

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

    public String ping() {
        // This exists by default in Spring Boot
        return restTemplate.getForObject(coordinatorUrl + "/actuator/health", String.class);
    }
}
