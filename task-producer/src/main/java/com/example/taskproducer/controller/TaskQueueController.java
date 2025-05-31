package com.example.taskproducer.controller;

import com.example.taskproducer.dto.TaskRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class TaskQueueController {

    private static final Logger logger = LoggerFactory.getLogger(TaskQueueController.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TOPIC = System.getProperty("app.kafka.topic", "task-queue-topic");

    @PostMapping("/tasks")
    public ResponseEntity<Map<String, Object>> submitTask(@RequestBody TaskRequest request){

        String taskId = UUID.randomUUID().toString();
        Map<String, Object> taskPayload = new HashMap<>();
        taskPayload.put("taskId", taskId);
        taskPayload.put("taskType", request.getTaskType());
        taskPayload.put("payload", request.getPayload());

        String message;
        try {
            message = objectMapper.writeValueAsString(taskPayload);
        } catch (JsonProcessingException e){
            logger.error("Failed to serialize task payload", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid task data");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, message);

        future.thenAccept(result -> {
            logger.info("Message posted successfully to task-queue-topic: " +  result.getRecordMetadata());
        }).exceptionally(ex -> {
            logger.error("Failed to post message to task-queue-topic", ex);
            return null;
        });

        Map<String, Object> ackResponse = new HashMap<>();
        ackResponse.put("message", "message send initiated");
        ackResponse.put("taskId", taskId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ackResponse);


    }
}
