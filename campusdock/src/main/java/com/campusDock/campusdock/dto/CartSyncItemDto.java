package com.campusDock.campusdock.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CartSyncItemDto {
    private UUID menuItemId;
    private int quantity;
}
