package com.example.taskconsumer.processor;

import java.util.Map;

public interface TaskProcessor {

    void process(Map<String, Object> payload);
}
