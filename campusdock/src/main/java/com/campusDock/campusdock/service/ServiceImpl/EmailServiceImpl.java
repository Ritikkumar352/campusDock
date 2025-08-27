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

        String userName = toEmail.substring(0, toEmail.indexOf("."));
        userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
        String htmlContent = """
                <!DOCTYPE html>
                <html>
                  <body style="font-family: Arial, sans-serif; padding: 16px; color: #333;">
                    <h2 style="color: #2E86C1;">Welcome to CampusDock, %s! 🎉</h2>
                    <p>Hi %s,</p>
                    <p>You're trying to sign in or sign up using your email address.</p>
                    <p>Please use the following OTP to verify your identity:</p>
                    <div style="background-color:#f4f4f4; border-left:4px solid #2E86C1; padding:16px; margin:16px 0;">
                      <h1 style="text-align:center; color:#2E86C1;">%s</h1>
                    </div>
                    <p>This code is valid for the next 10 minutes.</p>
                    <p>If you didn’t request this, you can safely ignore this email.</p>
                    <br>
                    <p style="font-size: 13px; color: #999;">Need help? Reach out at support@campusdock.live</p>
                    <p style="font-size: 13px; color: #999;">– Team CampusDock</p>
                  </body>
                </html>
                """.formatted(userName, userName, otp);


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

//        headers.add("X-Priority", "3"); // Normal priority
//        headers.add("X-Mailer", "CampusDock Mailer");
        HttpEntity<ResendEmailRequest> entity = new HttpEntity<>(requestBody, headers);

        //Send POST request to Resend:
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.resend.com/emails", entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email: " + response.getBody());
        }
    }
}
