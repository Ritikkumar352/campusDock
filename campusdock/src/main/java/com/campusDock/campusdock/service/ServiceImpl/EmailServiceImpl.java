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

        String htmlContent = "<h3>Welcome :) Your CampusDock OTP is:</h3><h1>" + otp + "</h1>";

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
