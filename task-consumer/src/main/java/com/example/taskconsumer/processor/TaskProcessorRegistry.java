package com.example.taskconsumer.processor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
//@RequiredArgsConstructor
public class TaskProcessorRegistry {

    private final Map<String, TaskProcessor> processorMap;

    public TaskProcessorRegistry(Map<String, TaskProcessor> processorMap){
        this.processorMap = processorMap;
    }

    public TaskProcessor getProcessor(String taskType){
        TaskProcessor processor = processorMap.get(taskType);
        if (processor == null){
            throw new IllegalArgumentException("No processor found for taskType: " + taskType);
        }

        return processor;
    }

    @PostConstruct
    public void init() {
        processorMap.forEach((taskType, processor) -> {
            System.out.println("Registered task processor for: " + taskType + " -> " + processor.getClass().getSimpleName());
        });
    }
}
