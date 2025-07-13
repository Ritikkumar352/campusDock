package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.Enum.OrderStatus;
import com.campusDock.campusdock.entity.Enum.PaymentStatus;
import com.campusDock.campusdock.entity.Order;
import com.campusDock.campusdock.entity.Payment;
import com.campusDock.campusdock.payment.PaymentGateway;
import com.campusDock.campusdock.repository.OrderRepo;
import com.campusDock.campusdock.repository.PaymentRepo;
import com.campusDock.campusdock.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;
    private final PaymentGateway paymentGateway;

    public PaymentServiceImpl(OrderRepo orderRepo, PaymentRepo paymentRepo, PaymentGateway paymentGateway) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public String initiatePayment(UUID orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Step 1: Trigger gateway (PhonePe)
        String paymentUrl = paymentGateway.initiatePayment(order.getId());

        // Step 2: Save payment entry
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepo.save(payment);

        // Step 3: Return payment URL
        return paymentUrl;
    }

    // 2.
    @Override
    public boolean handlePaymentCallback(Map<String, Object> payload) {
        // Extract orderId / transactionId from payload
        UUID orderId = UUID.fromString((String) payload.get("orderId"));
        String status = (String) payload.get("status"); // e.g., "SUCCESS", "FAILED"

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Find the latest payment for this order
        Payment payment = paymentRepo.findTopByOrderOrderByIdDesc(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if ("SUCCESS".equalsIgnoreCase(status)) {
            payment.setStatus(PaymentStatus.COMPLETED);
            order.setStatus(OrderStatus.COMPLETED);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
        }

        paymentRepo.save(payment);
        orderRepo.save(order);

        return true;
    }

    // 3.
    @Override
    @Transactional
    public void updatePaymentStatus(UUID orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = paymentRepo.findTopByOrderOrderByIdDesc(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepo.save(payment);

        order.setStatus(OrderStatus.COMPLETED);
        orderRepo.save(order);
    }





//    @Override
//    public Payment createPayment(UUID orderId, double amount) {
//        Order order = orderRepo.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
//
//        if (amount != order.getTotalAmount()) {
//            throw new IllegalArgumentException("Payment amount does not match the order total.");
//        }
//
//        Payment payment = Payment.builder()
//                .order(order)
//                .amount(amount)
//                .status(PaymentStatus.PENDING) // initial state
//                .build();
//
//        return paymentRepo.save(payment);
//    }
}
