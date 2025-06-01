package com.example.taskconsumer.kafka.consumer;

import com.example.taskconsumer.processor.TaskProcessorRegistry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DlqConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DlqConsumer.class);

    private final TaskProcessorRegistry registry;
    private final ObjectMapper objectMapper;

    public DlqConsumer(TaskProcessorRegistry registry, ObjectMapper objectMapper){
        this.registry = registry;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.dlq.topic.name}", groupId = "${kafka.dlq.group.id}")
    public void consumeDlqTask(String message) throws Exception{
        Map<String, Object> taskMap = objectMapper.readValue(message, new TypeReference<>() {});
        String taskType = (String) taskMap.get("taskType");
        Object payloadObj = taskMap.get("payload");
        Map<String, Object> payload = objectMapper.convertValue(payloadObj, new TypeReference<>() {});

        registry.getProcessor(taskType).process(payload);
        logger.info("Processed taskType from DLQ: {}", taskType);
    }
}
