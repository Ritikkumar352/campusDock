package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.Cart;
import com.campusDock.campusdock.entity.CartItem;
import com.campusDock.campusdock.entity.Enum.OrderStatus;
import com.campusDock.campusdock.entity.Order;
import com.campusDock.campusdock.entity.OrderItem;
import com.campusDock.campusdock.repository.CartRepo;
import com.campusDock.campusdock.repository.OrderItemRepo;
import com.campusDock.campusdock.repository.OrderRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final OrderItemRepo orderItemRepo;
    public OrderServiceImpl(OrderRepo orderRepo,CartRepo cartRepo,UserRepo userRepo, OrderItemRepo orderItemRepo) {
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.orderItemRepo = orderItemRepo;

    }


    // 1.
//    @Override
//    public void placeOrderFromCart(PlaceOrderRequest orderRequest){
//        UUID userId=orderRequest.getUserId();
//        UUID cartId=orderRequest.getCartId();
//
////        User user=userRepo.findById(userId).get();
//
//        Cart orderCart=cartRepo.findByIdAndUserId(cartId,userId);
//        System.out.println(orderCart.getCanteen()+" canteeennnn in place order");
//
//        List<CartItem> listOfItems=orderCart.getItems();
//        List<OrderItem> orderItem=new OrderItem();
//        double totalPrice = 0;
//        for(CartItem cartItem:listOfItems){
//            totalPrice += cartItem.getMenuItems().getPrice();
//
//        }
//
////        Order newOrder=new Order();
////        newOrder.setCanteen(orderCart.getCanteen());
////        newOrder.setUser(orderCart.getUser());
////        newOrder.setTotalAmount(totalPrice);
//        // payment and status ??
//
//
//    }

    // 2.


@Override
public UUID createOrderFromCart(UUID userId, UUID cartId) {
    Cart cart = cartRepo.findByIdAndUserId(cartId, userId);

    if (cart.getItems().isEmpty()) {
        throw new RuntimeException("Cart is empty");
    }

    // 1. Create Order
    Order order = new Order();
    order.setUser(cart.getUser());
    order.setCanteen(cart.getCanteen());
    order.setStatus(OrderStatus.PLACED);
    order.setOrderItems(new ArrayList<>());

    double totalAmount = 0.0;  //  [1] Init total

    // 2. Convert CartItems -> OrderItems
    for (CartItem cartItem : cart.getItems()) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItems(cartItem.getMenuItems());
        orderItem.setQuantity(cartItem.getQuantity());

        double subtotal = cartItem.getQuantity() * cartItem.getMenuItems().getPrice();
        orderItem.setSubtotal(subtotal);

        totalAmount += subtotal;               //  [2] Add to total
        order.getOrderItems().add(orderItem);  // [3] Attach item
    }

    order.setTotalAmount(totalAmount);         //  [4] Set total after loop

    // 3. Save Order (cascades to OrderItems)
    Order savedOrder = orderRepo.save(order);

    // 4. Clear the cart
    cartRepo.delete(cart);

    return savedOrder.getId();
}



    public ResponseEntity<?> getOrderDetails(UUID orderId){
        Order order = orderRepo.findById(orderId).get();
        return ResponseEntity.ok(order);
    }


}
