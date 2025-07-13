package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.CreateUserDto;
import com.campusDock.campusdock.dto.OtpResponse;
import com.campusDock.campusdock.dto.OtpResponseStatus;
import com.campusDock.campusdock.service.AuthService;
import com.campusDock.campusdock.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController( AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/sendOTP")
    public ResponseEntity<OtpResponse> sendOtp(@RequestBody CreateUserDto request) {
        return authService.sendOTP(request);
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<OtpResponseStatus> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return authService.verify(email,otp);
    }




}
