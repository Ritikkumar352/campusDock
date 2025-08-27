package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.CartDTO;
import com.campusDock.campusdock.dto.CartMapper;
import com.campusDock.campusdock.dto.CartSyncItemDto;
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


    @Override
    public CartDTO getCart(UUID userId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return CartMapper.toCartDTO(cart);
    }

    @Override
    public CartDTO addItem(UUID userId, UUID menuItemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // Get menu item and validate availability
        MenuItems menuItem = menuItemsRepo.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        if (!menuItem.isAvailable()) {
            throw new IllegalArgumentException("Item is currently unavailable");
        }

        // Get existing cart or create one
        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId, menuItemId)); // First-time cart

        // Validation: Ensure all items in the cart belong to the same canteen
        if (!cart.getItems().isEmpty()) {
            UUID existingCanteenId = cart.getItems().get(0).getMenuItems().getCanteen().getId();
            UUID newItemCanteenId = menuItem.getCanteen().getId();

            if (!existingCanteenId.equals(newItemCanteenId)) {
                throw new IllegalArgumentException(
                        "You can only add items from the same canteen to your cart. Please checkout or clear the cart first."
                );
            }
        }

        // Check if item already exists in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getMenuItems().getId().equals(menuItemId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setMenuItems(menuItem);
            newItem.setQuantity(quantity);
            newItem.setStatus(CartItemStatus.ACTIVE);
            cart.getItems().add(newItem);
        }

        cartRepo.save(cart);
        return CartMapper.toCartDTO(cart);
    }

    @Override
    public CartDTO updateQuantity(UUID userId, UUID menuItemId, int quantity) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().stream()
                .filter(i -> i.getMenuItems().getId().equals(menuItemId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(quantity));

        cartRepo.save(cart);
        return CartMapper.toCartDTO(cart);
    }

    @Override
    public CartDTO removeItem(UUID userId, UUID menuItemId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(i -> i.getMenuItems().getId().equals(menuItemId));

        cartRepo.save(cart);
        return CartMapper.toCartDTO(cart);
    }

    @Override
    public void syncCart(UUID userId, List<CartSyncItemDto> items) {
        // 1. Find the user and their associated cart
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // 2. Clear the existing items from the server-side cart.
        // The 'orphanRemoval=true' in your Cart entity will delete them from the database.
        cart.getItems().clear();

        // 3. Rebuild the cart from the items sent by the mobile app
        for (CartSyncItemDto syncItemDto : items) {
            // Find the corresponding menu item from the database
            MenuItems menuItem = menuItemsRepo.findById(syncItemDto.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found: " + syncItemDto.getMenuItemId()));

            // Create a new CartItem entity
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setMenuItems(menuItem);
            newCartItem.setQuantity(syncItemDto.getQuantity());
            newCartItem.setStatus(CartItemStatus.ACTIVE); // Set a default status

            // Add the new item to the cart's list
            cart.getItems().add(newCartItem);
        }

        // 4. Save the updated cart.
        // Because of @Transactional, this will save the cart and all the new CartItem entities.
        cartRepo.save(cart);

    }

    private Cart createCartForUser(UUID userId, UUID menuItemId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MenuItems menuItem = menuItemsRepo.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCanteen(menuItem.getCanteen());
        cart.setItems(new ArrayList<>());
        return cartRepo.save(cart);
    }
}

//
/// / 1. get user cart
//@Override
//public Cart getOrCreateUserCart(UUID userId) {
//    return cartRepo.findByUserId(userId)
//            .orElseGet(() -> {
//                User user = userRepo.findById(userId)
//                        .orElseThrow(() -> new RuntimeException("User not found"));
//                Cart newCart = new Cart();
//                newCart.setUser(user);
//
//                newCart.setItems(new ArrayList<>());
////                    System.out.println(newCart.getCanteen().getId()+" canteen id ");  // remove
//                System.out.println("canteennnn-> method 1");
//                return cartRepo.save(newCart);
//
//            });
//}
//
//// 2. Add menu item to cart or update cart
//@Override
//public String addOrUpdateCartItem(UUID userId, UUID menuItemId, int quantity, boolean forceClear) {
//    User user = userRepo.findById(userId)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//
//    MenuItems menuItem = menuItemsRepo.findById(menuItemId)
//            .orElseThrow(() -> new RuntimeException("Menu item not found"));
//
//    Cart cart = cartRepo.findByUserId(userId)
//            .orElseGet(() -> {
//                Cart newCart = new Cart();
//                newCart.setUser(user);
//                newCart.setCanteen(menuItem.getCanteen());   // set canteen of a cart
//                return cartRepo.save(newCart);
//            });
//
//    //        Fix for existing cart with no canteen
//    if (cart.getCanteen() == null) {
//        cart.setCanteen(menuItem.getCanteen());
//        cart = cartRepo.save(cart);
//    }
//
//    // BLOCK if cart already has different canteen
//    if (cart.getCanteen() != null && !cart.getCanteen().getId().equals(menuItem.getCanteen().getId())) {
//        if (!forceClear) {
//            return "DIFFERENT_CANTEEN"; // 🔴 Signal to frontend: show dialog
//        } else {
//            clearCart(userId); // clear cart
//            cart = new Cart();
//            cart.setUser(user);
//            cart.setCanteen(menuItem.getCanteen());
//            cart = cartRepo.save(cart);
//        }
//    }
//
//
//    Optional<CartItem> existingItem = cart.getItems().stream()
//            .filter(item -> item.getMenuItems().getId().equals(menuItemId))
//            .findFirst();
//
//    if (existingItem.isPresent()) {
//        CartItem item = existingItem.get();
//        item.setQuantity(item.getQuantity() + quantity);
//        cartItemRepo.save(item);
//    } else {
//        CartItem newItem = new CartItem();
//        newItem.setCart(cart);
//        newItem.setMenuItems(menuItem);
//        newItem.setQuantity(quantity);
//        newItem.setStatus(CartItemStatus.ADDED);
//        cartItemRepo.save(newItem);
//    }
//
//    return "Cart item added or updated successfully";
//}
//
//
//// 3. Delete an item from the cart
//@Override
//public void removeItemFromCart(UUID itemId) {
//    CartItem item = cartItemRepo.findById(itemId)
//            .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + itemId));
//    cartItemRepo.delete(item);
//}
//
//// 4. Get User Cart as DTO (used in controller)
//// TODO -> handle error when there is no user cart avl
//public CartResponseDto getUserCartDto(UUID userId) {
//    Cart cart = getOrCreateUserCart(userId);
//
//    List<CartItemDto> items = new ArrayList<>();
////        System.out.println(cart.getCanteen().getId()+" canteen id method --4");   // remove .. return canteen id also
//    System.out.println("canteennnn-> method 4");
//    for (CartItem item : cart.getItems()) {
//        MenuItems menu = item.getMenuItems();
//        items.add(new CartItemDto(
//                item.getId(),             // cartItem ID
//                menu.getId(),             // menuItem ID (this was incorrect earlier)
//                menu.getFoodName(),
//                menu.getPrice(),
//                item.getQuantity()
//        ));
//    }
//
//    UUID canteenId = cart.getCanteen() != null ? cart.getCanteen().getId() : null;
//
//
//    return new CartResponseDto(
//            cart.getId(),
//            cart.getUser().getId(),
//            canteenId,
//            items
//    );
//}
//
//// 5. Clear whole cart (to swithc canteen) .. TODO -> later add to archive this card [Multiple cart for diff canteens]
//public boolean clearCart(UUID userId) {
//    Optional<Cart> userCart=cartRepo.findByUserId(userId);
//    if(userCart.isPresent()) {
//        cartRepo.delete(userCart.get());
//        return true;
//    } else {
//        return false;
//    }
//}
