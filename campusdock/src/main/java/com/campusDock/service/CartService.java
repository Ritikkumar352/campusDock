package com.campusDock.service;

import com.campusDock.entity.Cart;

import java.util.UUID;

public interface CartService {

    // 1. get A user Cart
    Cart getOrCreateUserCart(UUID userId);

    // 2. add items to cart
    String addOrUpdateCartItem(UUID userId, UUID menuItemId, int quantity);

    // 3. Delete an item from the cart
    void removeItemFromCart(UUID itemId);

}
