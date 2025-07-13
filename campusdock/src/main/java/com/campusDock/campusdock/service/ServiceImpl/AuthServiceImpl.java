package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.CreateUserDto;
import com.campusDock.campusdock.dto.OtpResponse;
import com.campusDock.campusdock.dto.OtpResponseStatus;
import com.campusDock.campusdock.service.AuthService;
import com.campusDock.campusdock.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService
{
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final EmailService emailService;

    public AuthServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<OtpResponse> sendOTP(CreateUserDto request) {
        String email = request.getEmail();
        String otp = generateOtp();

        // Save OTP for this email temporarily  -- TODO - Use Redis here
        otpStorage.put(email, otp);

        emailService.sendOtpEmail(email, otp);


        return ResponseEntity.ok(
                OtpResponse.builder()
                        .message("OTP sent to " + email)
                        .build()
        );
    }

    @Override
    public ResponseEntity<OtpResponseStatus> verify(String email, String otp) {
        String storedOtp = otpStorage.get(email);

        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email); // discard OTP

            return ResponseEntity.ok(
                    OtpResponseStatus.builder()
                            .success(true)
                            .message("OTP verified successfully")
                            .build()
            );
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(OtpResponseStatus.builder()
                            .success(false)
                            .message("Invalid or expired OTP")
                            .build()
                    );
        }
    }

    private String generateOtp() {
        int otp = new Random().nextInt(9000) + 1000; // 4-digit
        return String.valueOf(otp);
    }
}
