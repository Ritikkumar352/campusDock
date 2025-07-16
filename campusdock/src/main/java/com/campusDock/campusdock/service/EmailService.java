package com.campusDock.campusdock.service;

import com.campusDock.campusdock.service.ServiceImpl.EmailServiceImpl;

public interface EmailService  {
    void sendOtpEmail(String toEmail, String otp);
}
