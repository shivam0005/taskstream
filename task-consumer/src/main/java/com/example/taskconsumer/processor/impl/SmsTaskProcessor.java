package com.example.taskconsumer.processor.impl;

import com.example.taskconsumer.exception.TaskProcessingException;
import com.example.taskconsumer.processor.TaskProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component("sms")
public class SmsTaskProcessor implements TaskProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SmsTaskProcessor.class);

    private final RestTemplate restTemplate;

    @Value("${notification.sms.url}")
    private String notificationServiceUrl;


    public SmsTaskProcessor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void process(Map<String, Object> payload) {
        try {
            String name = (String) payload.get("name");
            String mobile = (String) payload.get("mobile");

            Map<String, String> smsRequest = Map.of(
                    "name", name,
                    "mobile", mobile
            );

            restTemplate.postForEntity(notificationServiceUrl, smsRequest, String.class);

            logger.info("SMS task sent to notification service for mobile: {}", mobile);
        } catch (Exception e) {
            logger.error("Failed to process SMS task", e);
            throw new RuntimeException("Failed to process SMS task", e); // This will push to DLQ if enabled
        }
    }
}
