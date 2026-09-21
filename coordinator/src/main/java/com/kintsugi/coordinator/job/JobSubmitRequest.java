package com.kintsugi.coordinator.job;

import lombok.Data;

@Data
public class JobSubmitRequest {
    private String type;
    private String payload;
    private int priority;
    private int maxRetries;
}