package com.campusDock.campusdock.service;

import com.campusDock.campusdock.dto.CreateUserDto;
import com.campusDock.campusdock.dto.OtpResponse;
import com.campusDock.campusdock.dto.OtpResponseStatus;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<OtpResponse> sendOTP(CreateUserDto request);

    ResponseEntity<OtpResponseStatus> verify(String email, String otp);
}
