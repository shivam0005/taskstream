package com.example.notification.controller;

import com.example.notification.dto.EmailRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender){
        this.mailSender=mailSender;
    }


    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getBody());

            mailSender.send(message);

            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email failed: " + e.getMessage());
        }
    }
}
