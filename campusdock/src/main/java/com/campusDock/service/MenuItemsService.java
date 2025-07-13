package com.campusDock.service;

import com.campusDock.dto.MenuItemRequestDto;
import com.campusDock.entity.MenuItems;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public interface MenuItemsService {
    // 1.
    ResponseEntity<Map<String, String>> addMenuItem(UUID canteenId, MenuItemRequestDto dto, MultipartFile file);

    // 2.
    ResponseEntity<MenuItems> getMenuItem(UUID id);
}
