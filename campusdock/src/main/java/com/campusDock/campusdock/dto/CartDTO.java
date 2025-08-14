package com.campusDock.campusdock.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class CartDTO {
    private UUID cartId;
    private UUID canteenId;
    private String canteenName;
    private List<CartItemDto> items;
    private double totalAmount;
}
