package com.campusDock.campusdock.payment.impl;



import com.campusDock.campusdock.payment.PaymentGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PhonePePaymentService implements PaymentGateway {

    @Value("${phonepe.client.id}")
    private String clientId;

    @Value("${phonepe.client.secret}")
    private String clientSecret;

    @Value("${phonepe.api.url}")
    private String phonePeUrl;

    @Override
    public String initiatePayment(UUID orderId) {
        // TODO: Construct payload with amount, orderId, and redirect/callback URL
        // TODO: Sign it with client secret
        // TODO: Send HTTP POST request to PhonePe API
        // TODO: Return the payment link

        return "https://mock.phonepe.com/pay/" + orderId;  // placeholder
    }

    @Override
    public boolean verifyPayment(String paymentId) {
        // TODO: Implement payment status check via PhonePe API if needed
        return true; // assume success for now
    }
}
