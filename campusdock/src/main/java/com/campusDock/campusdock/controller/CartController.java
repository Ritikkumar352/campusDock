package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.CartDTO;
import com.campusDock.campusdock.dto.CartSyncItemDto;
import com.campusDock.campusdock.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<?> getCart(@RequestParam UUID userId)    //Tested and working
    {
        try {
            CartDTO cart = cartService.getCart(userId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(
            @RequestParam UUID userId,
            @RequestParam UUID menuItemId,
            @RequestParam int quantity) {                                                             //Tested and working
        try {
            CartDTO cart = cartService.addItem(userId, menuItemId, quantity);
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/update")
    public CartDTO updateItemQuantity(
            @RequestParam UUID userId,
            @RequestParam UUID menuItemId,
            @RequestParam int quantity
    ) {                                                             //Tested and TODO-- quantity minus me jaa rahi hai...Fix it with validation
        return cartService.updateQuantity(userId, menuItemId, quantity);
    }

    @DeleteMapping("/remove")
    public CartDTO removeItem(
            @RequestParam UUID userId,
            @RequestParam UUID menuItemId
    ) {                                                             //Tested and working
        return cartService.removeItem(userId, menuItemId);
    }

    @PostMapping("/sync")
    public ResponseEntity<?> syncCart(
            @RequestParam UUID userId,
            @RequestBody List<CartSyncItemDto> items) {
        try {
            cartService.syncCart(userId, items);
            // Return 200 OK with a success message
            return ResponseEntity.ok(Map.of("message", "Cart synced successfully"));
        } catch (RuntimeException e) {
            // Handle cases like user not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }


    //TODO---- Manual testing for trying to add differnt canteen menuitem in single cart is left

}


//
//
//
/// / 1. Get cart of a user (add logged-in user )
//@GetMapping("/{userId}")
//public ResponseEntity<CartResponseDto> getUserCart(@PathVariable UUID userId) {
//    return ResponseEntity.ok(cartService.getUserCartDto(userId));
//}
//
//// 2. Add or upate cart of a user
//@PostMapping("/add")
//public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request) {
//    String message = cartService.addOrUpdateCartItem(
//            request.getUserId(),
//            request.getMenuItemId(),
//            request.getQuantity(),
//            request.isForceClear()
//    );
//    if ("DIFFERENT_CANTEEN".equals(message)) {    // clear cart and addd that item or Cancle
//        return ResponseEntity.status(409).body("Cart contains items from a different canteen");
//    }
//    return ResponseEntity.ok(message);
//}
//
//// 3. Delete an item from the cart
//@DeleteMapping("/remove/{itemId}")    // TODO -> if quantity is 2,3,4,.... it's removing all of that item at once FIX
//// TODO -> need to add another method for this -> decrease quantity -> delete item is OK ig
//public ResponseEntity<String> removeItemFromCart(@PathVariable UUID itemId) {
//    cartService.removeItemFromCart(itemId);
//    return ResponseEntity.ok("Item removed from cart successfully");
//}
//
//// 4. Clear Cart of a User
//public ResponseEntity<String > clearCart(UUID userId){
//    if(cartService.clearCart(userId)){
//        return ResponseEntity.ok("Cart cleared successfully");
//    }else {
//        return ResponseEntity.badRequest().body("Cart cleared failed");
//    }
//}