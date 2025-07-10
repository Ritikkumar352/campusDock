package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.PlaceOrderRequest;
import com.campusDock.campusdock.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
    
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 1.
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody PlaceOrderRequest request) {
        try {
            UUID orderId = orderService.createOrderFromCart(request.getUserId(), request.getCartId());
            return ResponseEntity.ok("Order created successfully with ID: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create order: " + e.getMessage());
        }
    }

    //2. Order Details -> Improve this .. DTO , error handling or....
    @GetMapping("/details/{orderId}")
    public ResponseEntity<?> details(@PathVariable UUID orderId) {
        return orderService.getOrderDetails(orderId);
    }

}
