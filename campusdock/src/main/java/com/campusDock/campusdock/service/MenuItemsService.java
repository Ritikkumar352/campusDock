package com.campusDock.campusdock.service;

import com.campusDock.campusdock.entity.DTO.MenuItemRequestDto;
import com.campusDock.campusdock.entity.MenuItems;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public interface MenuItemsService {
    // 1.
    ResponseEntity<Map<String, String>> addMenuItem(MenuItemRequestDto dto, MultipartFile file);

    // 2.
    ResponseEntity<MenuItems> getMenuItem(UUID id);
}
