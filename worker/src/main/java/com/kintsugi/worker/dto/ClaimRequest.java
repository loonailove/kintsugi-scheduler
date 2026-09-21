package com.kintsugi.worker.dto;

public class ClaimRequest {
    private String workerId;

    public ClaimRequest() {}

    public ClaimRequest(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkerId() { return workerId; }
    public void setWorkerId(String workerId) { this.workerId = workerId; }
}
