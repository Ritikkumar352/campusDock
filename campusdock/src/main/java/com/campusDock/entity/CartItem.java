package com.campusDock.entity;



import com.campusDock.entity.Enum.CartItemStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private MenuItems menuItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CartItemStatus status = CartItemStatus.ACTIVE;

    @Column(nullable = false)
    private int quantity;

}

