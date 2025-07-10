package com.campusDock.campusdock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRequest {
    private UUID userId;
    private UUID cartId;
    // No need to send menu items explicitly (we'll use cart content)
}