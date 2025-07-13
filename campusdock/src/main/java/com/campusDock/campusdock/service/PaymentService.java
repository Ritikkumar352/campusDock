package com.campusDock.campusdock.service;


import com.campusDock.campusdock.entity.Payment;

import java.util.Map;
import java.util.UUID;

public interface PaymentService {
    //1.
//    Payment createPayment(UUID orderId, double amount);

    String initiatePayment(UUID orderId);

    // 2.
    boolean handlePaymentCallback(Map<String, Object> payload);

    // 3.
    void updatePaymentStatus(UUID orderId);

}
