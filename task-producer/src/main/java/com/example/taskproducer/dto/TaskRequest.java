package com.example.taskproducer.dto;

import lombok.Data;

@Data
public class TaskRequest {

    String taskType;
    String payload;
}
