package com.campusDock.controller;

import com.campusDock.dto.AddToCartRequest;
import com.campusDock.entity.Cart;
import com.campusDock.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 1. Get cart of a user (add logged-in user )
    @GetMapping("/{userId}")     //
    public ResponseEntity<Cart> getUserCart(@PathVariable UUID userId) {
        Cart cart = cartService.getOrCreateUserCart(userId);
        return ResponseEntity.ok(cart);
    }

    // 2. Add or upate cart of a user
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request) {
        String message = cartService.addOrUpdateCartItem(
                request.getUserId(),
                request.getMenuItemId(),
                request.getQuantity()
        );
        return ResponseEntity.ok(message);
    }

    // 3. Delete an item from the cart
    @DeleteMapping("/remove/{itemId}")    // TODO -> if quantity is 2,3,4,.... it's removing all of that item at once FIX
    public ResponseEntity<String> removeItemFromCart(@PathVariable UUID itemId) {
        cartService.removeItemFromCart(itemId);
        return ResponseEntity.ok("Item removed from cart successfully");
    }


}
