package com.campusDock.campusdock.dto;

import com.campusDock.campusdock.entity.Cart;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartDTO toCartDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getId());
        dto.setCanteenId(cart.getCanteen().getId());
        dto.setCanteenName(cart.getCanteen().getName());

        dto.setItems(
                cart.getItems().stream().map(item -> {
                    CartItemDto itemDTO = new CartItemDto();
                    itemDTO.setId(item.getId());
                    itemDTO.setMenuItemId(item.getMenuItems().getId());
                    itemDTO.setFoodName(item.getMenuItems().getFoodName());
                    itemDTO.setPrice(item.getMenuItems().getPrice());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setStatus(item.getStatus().name());
                    return itemDTO;
                }).collect(Collectors.toList())
        );

        dto.setTotalAmount(
                cart.getItems().stream()
                        .mapToDouble(i -> i.getMenuItems().getPrice() * i.getQuantity())
                        .sum()
        );

        return dto;
    }
}

