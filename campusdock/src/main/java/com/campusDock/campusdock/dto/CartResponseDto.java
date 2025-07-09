package com.campusDock.campusdock.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {
    private UUID cartId;
    private UUID userId;
    private List<CartItemDto> items;
}
