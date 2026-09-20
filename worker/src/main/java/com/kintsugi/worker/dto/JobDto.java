package com.kintsugi.worker.dto;

import java.util.UUID;

public class JobDto {
    private UUID id;
    private String type;
    private String payload;
    private String status;
    private int priority;

    public JobDto() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}