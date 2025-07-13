package com.campusDock.campusdock.payment;



import java.util.UUID;

public interface PaymentGateway {
    String initiatePayment(UUID orderId);
    boolean verifyPayment(String paymentId);
}
