package com.campusDock.campusdock.service;

import com.campusDock.campusdock.dto.CartDTO;
import com.campusDock.campusdock.dto.CartResponseDto;
import com.campusDock.campusdock.dto.CartSyncItemDto;
import com.campusDock.campusdock.entity.Cart;

import java.util.List;
import java.util.UUID;

public interface CartService {


    CartDTO getCart(UUID userId);

    CartDTO addItem(UUID userId, UUID menuItemId, int quantity);

    CartDTO updateQuantity(UUID userId, UUID menuItemId, int quantity);

    CartDTO removeItem(UUID userId, UUID menuItemId);

    void syncCart(UUID userId, List<CartSyncItemDto> items);
}

//// 1. get A user Cart
//Cart getOrCreateUserCart(UUID userId);
//
//// 2. add items to cart
//String addOrUpdateCartItem(UUID userId, UUID menuItemId, int quantity, boolean forceClear);
//
//// 3. Delete an item from the cart
//void removeItemFromCart(UUID itemId);
//
//// 4.
//CartResponseDto getUserCartDto(UUID userId);
//
//// 5.
//boolean clearCart(UUID userId);
