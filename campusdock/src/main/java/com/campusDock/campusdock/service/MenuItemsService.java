package com.campusDock.campusdock.service;

import com.campusDock.campusdock.dto.DetailedMenuItemDto;
import com.campusDock.campusdock.dto.MenuItemRequestDto;
import com.campusDock.campusdock.entity.MenuItems;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MenuItemsService {
    // 1.
    ResponseEntity<Map<String, String>> addMenuItem(
            UUID canteenId,
            MenuItemRequestDto dto,
            List<MultipartFile> files
    ) ;

    // 2.
    DetailedMenuItemDto getMenuItem(UUID id);
}
