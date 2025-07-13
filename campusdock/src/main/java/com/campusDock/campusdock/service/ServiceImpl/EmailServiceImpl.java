package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.ResendEmailRequest;
import com.campusDock.campusdock.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.sender.email}")
    private String senderEmail;




    public void sendOtpEmail(String toEmail, String otp) {

            String htmlContent = """
        <div style="font-family: Arial, sans-serif; padding: 16px;">
            <h2 style="color: #2E86C1;">Welcome to CampusDock 🎉</h2>
            <p>We're excited to have you onboard. Use the OTP below to verify your email:</p>
            
            <div style="margin: 20px 0; padding: 16px; background-color: #f4f4f4; border-left: 4px solid #2E86C1;">
                <h1 style="margin: 0; font-size: 32px; color: #2E86C1; text-align: center;">%s</h1>
            </div>
            
            <p style="color: #555;">This OTP is valid for the next 10 minutes. Please do not share it with anyone.</p>
            
            <br/>
            <p style="font-size: 14px; color: #999;">- Team CampusDock</p>
        </div>
    """.formatted(otp);


        ResendEmailRequest requestBody = ResendEmailRequest.builder()
                .from(senderEmail)
                .to(toEmail)
                .subject("CampusDock Email Verification")
                .html(htmlContent)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ResendEmailRequest> entity = new HttpEntity<>(requestBody, headers);

        //Send POST request to Resend:
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.resend.com/emails", entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email: " + response.getBody());
        }
    }
}
