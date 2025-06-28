package com.campusDock.campusdock.repository;

import com.campusDock.campusdock.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepo extends JpaRepository<CartItem, UUID> {
}
