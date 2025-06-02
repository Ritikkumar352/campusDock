package com.campusDock.campusdock.entity;



import com.campusDock.campusdock.entity.Enum.CartItemStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    @JsonBackReference
    private MenuItems menuItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CartItemStatus status;

    private int quantity;

    public CartItem(Cart cart, UUID id, MenuItems menuItems, int quantity, CartItemStatus status) {
        this.cart = cart;
        this.id = id;
        this.menuItems = menuItems;
        this.quantity = quantity;
        this.status = status;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MenuItems getMenuItem() {
        return menuItems;
    }

    public void setMenuItem(MenuItems menuItems) {
        this.menuItems = menuItems;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartItemStatus getStatus() {
        return status;
    }

    public void setStatus(CartItemStatus status) {
        this.status = status;
    }
}
