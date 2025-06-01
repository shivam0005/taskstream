package com.example.taskconsumer.processor.impl;

import com.example.taskconsumer.exception.TaskProcessingException;
import com.example.taskconsumer.processor.TaskProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("email")
//@RequiredArgsConstructor
public class EmailTaskProcessor implements TaskProcessor {

    private final JavaMailSender mailSender;

    @Override
    public void process(Map<String, Object> payload) {

        try {
            String to = (String) payload.get("to");
            String subject = (String) payload.get("subject");
            String body = (String) payload.get("body");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);


            mailSender.send(message);
        } catch (Exception e){
            throw new TaskProcessingException("Failed to send email", e);
        }

    }

    public EmailTaskProcessor(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }
}
