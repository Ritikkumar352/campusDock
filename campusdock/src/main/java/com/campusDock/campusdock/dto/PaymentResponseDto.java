package com.campusDock.campusdock.dto;

import com.campusDock.campusdock.entity.Enum.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PaymentResponseDto {
    private UUID paymentId;
    private UUID orderId;
    private double amount;
    private PaymentStatus status;
}
