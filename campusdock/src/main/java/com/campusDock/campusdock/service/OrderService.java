package com.campusDock.campusdock.service;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface OrderService {
//    void placeOrderFromCart(PlaceOrderRequest orderRequest);
    UUID createOrderFromCart(UUID userId, UUID cartId);

    ResponseEntity<?> getOrderDetails(UUID orderId);  // TODO -> Remove response entity from serice
}
