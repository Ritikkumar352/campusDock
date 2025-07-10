package com.campusDock.campusdock.service;

import com.campusDock.campusdock.dto.CartResponseDto;
import com.campusDock.campusdock.entity.Cart;

import java.util.UUID;

public interface CartService {

    // 1. get A user Cart
    Cart getOrCreateUserCart(UUID userId);

    // 2. add items to cart
    String addOrUpdateCartItem(UUID userId, UUID menuItemId, int quantity, boolean forceClear);

    // 3. Delete an item from the cart
    void removeItemFromCart(UUID itemId);

    // 4.
    CartResponseDto getUserCartDto(UUID userId);

    // 5.
    boolean clearCart(UUID userId);

}
