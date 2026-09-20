package com.kintsugi.worker.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class JobDto {
    private UUID id;
    private String type;
    private String payload;
    private int priority;
}