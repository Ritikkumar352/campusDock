package com.campusDock.campusdock.repository;

import com.campusDock.campusdock.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepo extends JpaRepository<OrderItem, UUID> {
}
