package com.campusDock.campusdock.repository;

import com.campusDock.campusdock.entity.MenuItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuItemsRepo extends JpaRepository<MenuItems, UUID> {

    List<MenuItems> findByCanteen_Id(UUID canteenId);

}
