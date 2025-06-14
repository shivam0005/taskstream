package com.example.notification.controller;

import com.example.notification.dto.SmsRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
@Slf4j
public class SmsController {

    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${msg91.auth.key}")
    private String authKey;

    @Value("${msg91.template.id}")
    private String templateId;

    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestBody SmsRequest smsRequest) {
        String url = "https://control.msg91.com/api/v5/flow/";

        // Payload as per MSG91 Flow API
        Map<String, Object> payload = new HashMap<>();
        payload.put("template_id", templateId);
        payload.put("short_url", "0");


        Map<String, Object> recipient = new HashMap<>();
        recipient.put("mobiles", smsRequest.getMobile());
        recipient.put("name", smsRequest.getName());

        payload.put("recipients", List.of(recipient));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authkey", authKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            logger.info("sending to MSG91");
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            JsonNode responseJson = objectMapper.readTree(response.getBody());
            String type = responseJson.path("type").asText();

            if (!"success".equalsIgnoreCase(type)) {
                String error = responseJson.path("message").asText();
                logger.error("Msg91 failed: {}", error);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SMS failed: " + error);
            }
            return ResponseEntity.ok("SMS sent successfully");

        } catch (Exception e) {
            logger.error("Exception during SMS sending", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error sending SMS");
        }
    }

}
