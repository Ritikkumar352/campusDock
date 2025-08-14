package com.campusDock.campusdock.entity;



import com.campusDock.campusdock.entity.Enum.CartItemStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)   // eager because frontend will almost always need food details when fetching cart.
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference // So that we can return menu item details
    private MenuItems menuItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CartItemStatus status = CartItemStatus.ACTIVE;

    @Column(nullable = false)
    private int quantity;

}

