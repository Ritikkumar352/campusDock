package com.campusDock.repository;

import com.campusDock.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepo extends JpaRepository<CartItem, UUID> {
}
