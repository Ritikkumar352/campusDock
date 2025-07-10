package com.campusDock.campusdock.dto;


import com.campusDock.campusdock.entity.Canteen;
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
    private UUID canteenId;
    private List<CartItemDto> items;
}
