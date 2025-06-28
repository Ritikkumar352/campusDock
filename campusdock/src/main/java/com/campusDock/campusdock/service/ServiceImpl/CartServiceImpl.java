package com.campusDock.campusdock.service.ServiceImpl;

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
                    return cartRepo.save(newCart);
                });
    }

    // 2. Add menu item to cart or update cart
    @Override
    public String addOrUpdateCartItem(UUID userId, UUID menuItemId, int quantity) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MenuItems menuItem = menuItemsRepo.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepo.save(newCart);
                });

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

}
