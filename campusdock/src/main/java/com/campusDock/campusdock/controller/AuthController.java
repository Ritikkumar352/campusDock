package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.CreateUserDto;
import com.campusDock.campusdock.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final EmailService emailService;

    public AuthController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendOTP")
    public ResponseEntity<String> sendOtp(@RequestBody CreateUserDto request) {
        String email = request.getEmail();

        // Generate OTP (you can use a better secure method)
        String otp = "1234";

        // TODO: Save OTP in DB or cache for verification later

        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok("OTP sent to " + email);
    }

}
