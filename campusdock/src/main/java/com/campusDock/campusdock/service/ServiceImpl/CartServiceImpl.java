package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.CartItemDto;
import com.campusDock.campusdock.dto.CartResponseDto;
import com.campusDock.campusdock.entity.Cart;
import com.campusDock.campusdock.entity.CartItem;
import com.campusDock.campusdock.entity.Enum.CartItemStatus;
import com.campusDock.campusdock.entity.MenuItems;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CartItemRepo;
import com.campusDock.campusdock.repository.CartRepo;
import com.campusDock.campusdock.repository.MenuItemsRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.service.CartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final MenuItemsRepo menuItemsRepo;
    private final CartItemRepo cartItemRepo;
    public CartServiceImpl(
            CartRepo cartRepo,
            UserRepo userRepo,
            MenuItemsRepo menuItemsRepo,
            CartItemRepo cartItemRepo
    ) {
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.menuItemsRepo = menuItemsRepo;
        this.cartItemRepo = cartItemRepo;
    }

    // 1. get user cart
    @Override
    public Cart getOrCreateUserCart(UUID userId) {
        return cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepo.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Cart newCart = new Cart();
                    newCart.setUser(user);

                    newCart.setItems(new ArrayList<>());
//                    System.out.println(newCart.getCanteen().getId()+" canteen id ");  // remove
                    System.out.println("canteennnn-> method 1");
                    return cartRepo.save(newCart);

                });
    }

    // 2. Add menu item to cart or update cart
    @Override
    public String addOrUpdateCartItem(UUID userId, UUID menuItemId, int quantity, boolean forceClear) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MenuItems menuItem = menuItemsRepo.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCanteen(menuItem.getCanteen());   // set canteen of a cart
                    return cartRepo.save(newCart);
                });

        //        Fix for existing cart with no canteen
        if (cart.getCanteen() == null) {
            cart.setCanteen(menuItem.getCanteen());
            cart = cartRepo.save(cart);
        }

        // BLOCK if cart already has different canteen
        if (cart.getCanteen() != null && !cart.getCanteen().getId().equals(menuItem.getCanteen().getId())) {
            if (!forceClear) {
                return "DIFFERENT_CANTEEN"; // 🔴 Signal to frontend: show dialog
            } else {
                clearCart(userId); // clear cart
                cart = new Cart();
                cart.setUser(user);
                cart.setCanteen(menuItem.getCanteen());
                cart = cartRepo.save(cart);
            }
        }


        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getMenuItems().getId().equals(menuItemId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepo.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setMenuItems(menuItem);
            newItem.setQuantity(quantity);
            newItem.setStatus(CartItemStatus.ADDED);
            cartItemRepo.save(newItem);
        }

        return "Cart item added or updated successfully";
    }


    // 3. Delete an item from the cart
    @Override
    public void removeItemFromCart(UUID itemId) {
        CartItem item = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + itemId));
        cartItemRepo.delete(item);
    }

    // 4. Get User Cart as DTO (used in controller)
    // TODO -> handle error when there is no user cart avl
    public CartResponseDto getUserCartDto(UUID userId) {
        Cart cart = getOrCreateUserCart(userId);

        List<CartItemDto> items = new ArrayList<>();
//        System.out.println(cart.getCanteen().getId()+" canteen id method --4");   // remove .. return canteen id also
        System.out.println("canteennnn-> method 4");
        for (CartItem item : cart.getItems()) {
            MenuItems menu = item.getMenuItems();
            items.add(new CartItemDto(
                    item.getId(),             // cartItem ID
                    menu.getId(),             // menuItem ID (this was incorrect earlier)
                    menu.getFoodName(),
                    menu.getPrice(),
                    item.getQuantity()
            ));
        }

        UUID canteenId = cart.getCanteen() != null ? cart.getCanteen().getId() : null;


        return new CartResponseDto(
                cart.getId(),
                cart.getUser().getId(),
                canteenId,
                items
        );
    }

    // 5. Clear whole cart (to swithc canteen) .. TODO -> later add to archive this card [Multiple cart for diff canteens]
    public boolean clearCart(UUID userId) {
        Optional<Cart> userCart=cartRepo.findByUserId(userId);
        if(userCart.isPresent()) {
            cartRepo.delete(userCart.get());
            return true;
        } else {
            return false;
        }
    }


}
