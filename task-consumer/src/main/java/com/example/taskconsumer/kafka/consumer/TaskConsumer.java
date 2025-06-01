package com.example.taskconsumer.kafka.consumer;

import com.example.taskconsumer.processor.TaskProcessorRegistry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
//@RequiredArgsConstructor
public class TaskConsumer {

        private static final Logger logger = LoggerFactory.getLogger(TaskConsumer.class);

        private final TaskProcessorRegistry registry;
        private final ObjectMapper objectMapper;

        public TaskConsumer(TaskProcessorRegistry registry, ObjectMapper objectMapper){
            this.registry = registry;
            this.objectMapper = objectMapper;
        }

        @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group.id}")
        public void consumeTask(String message) throws Exception{
            Map<String, Object> taskMap = objectMapper.readValue(message, new TypeReference<>() {});
            String taskType = (String) taskMap.get("taskType");
            Object payloadObj = taskMap.get("payload");
            Map<String, Object> payload = objectMapper.convertValue(payloadObj, new TypeReference<>() {});

                // Simulate failure if payload contains simulateFail=true
            if (payload.getOrDefault("simulateFail", false).equals(true)) {
                throw new RuntimeException("Simulated failure for testing DLQ");
            }

            registry.getProcessor(taskType).process(payload);
            logger.info("Processed taskType: {}", taskType);
        }
}
