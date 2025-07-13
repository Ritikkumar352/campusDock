package com.campusDock.campusdock.repository;

import com.campusDock.campusdock.entity.Order;
import com.campusDock.campusdock.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {

//    Optional<Object> findTopByOrderOrderByIdDesc(Order order);
    Optional<Payment> findTopByOrderOrderByIdDesc(Order order);


}
