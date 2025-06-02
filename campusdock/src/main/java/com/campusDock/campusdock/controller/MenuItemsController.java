package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.entity.DTO.MenuItemRequestDto;
import com.campusDock.campusdock.entity.MenuItems;
import com.campusDock.campusdock.service.ServiceImpl.MenuItemServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menuItems")
public class MenuItemsController {

    private final MenuItemServiceImpl menuItemService;
    public MenuItemsController(MenuItemServiceImpl menuItemService) {
        this.menuItemService = menuItemService;
    }


    // 1. Add menu item
    @PostMapping
    public ResponseEntity<Map<String,String>> addMenuItem(
            @RequestPart(value = "menuItem", required = true) MenuItemRequestDto dto,
            @RequestPart(value = "file",required = false) MultipartFile file
    ) {
         return  menuItemService.addMenuItem(dto,file);
    }

    // 2. Get Menu Item
    @GetMapping("/{id}")
    public ResponseEntity<MenuItems> getMenuItemById(@PathVariable("id") UUID id) {
        return menuItemService.getMenuItem(id);
    }



}
