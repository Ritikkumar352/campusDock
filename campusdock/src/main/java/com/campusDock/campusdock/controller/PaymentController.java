package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


//      Trigger payment for an order

    @PostMapping("/initiate/{orderId}")
    public ResponseEntity<?> initiatePayment(@PathVariable UUID orderId) {
        try {
            String paymentUrl = paymentService.initiatePayment(orderId);

            return ResponseEntity.ok(Map.of(
                    "message", "Payment initiated successfully",
                    "paymentUrl", paymentUrl,
                    "orderId", orderId
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected server error"));
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<?> handlePhonePeCallback(@RequestBody Map<String, Object> payload) {
        try {
            boolean success = paymentService.handlePaymentCallback(payload);

            if (success) {
                return ResponseEntity.ok(Map.of("message", "Payment processed successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Failed to process payment"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error processing callback"));
        }
    }

    @GetMapping("/update-status/{orderId}")
    public ResponseEntity<?> updateStatus(@PathVariable UUID orderId) {
        try {
            paymentService.updatePaymentStatus(orderId);
            return ResponseEntity.ok(Map.of(
                    "message", "Payment and order status updated to COMPLETED",
                    "orderId", orderId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }


}
