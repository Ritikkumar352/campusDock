package com.campusDock.campusdock.repository;

import com.campusDock.campusdock.entity.Cart;
import com.campusDock.campusdock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepo extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUserId(UUID userId);
    Cart findByIdAndUserId( UUID cartId,UUID userId);
    Optional<Cart> findByUser(User user);

}
