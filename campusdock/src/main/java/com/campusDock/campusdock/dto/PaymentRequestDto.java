package com.campusDock.campusdock.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequestDto {
    private UUID orderId;
    private double amount;
}
