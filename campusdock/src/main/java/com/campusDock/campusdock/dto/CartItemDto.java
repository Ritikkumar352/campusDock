package com.campusDock.campusdock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private UUID id;
    //private UUID itemId;        // cartItem ID
    private UUID menuItemId;    // menuItem ID (used to fetch menu later)
    private String foodName;
    private double price;
    private int quantity;
    private String status;
    private String url;
}