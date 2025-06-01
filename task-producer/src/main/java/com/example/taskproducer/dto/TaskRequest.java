package com.example.taskproducer.dto;

import lombok.Data;

import java.util.Map;

@Data
public class TaskRequest {

    private String taskType;
    private Map<String, Object> payload;
}
