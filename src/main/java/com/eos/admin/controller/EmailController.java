package com.eos.admin.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eos.admin.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService; // Replace with your actual service class name

//    @PostMapping("/send-interview-email")
//    public ResponseEntity<String> sendInterviewEmail(@RequestParam String toEmail) {
//        try {
//            emailService.sendScheduleInterviewEmailToManager(toEmail);
//            return ResponseEntity.ok("Interview schedule email sent to: " + toEmail);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
//        }
//    }
}
