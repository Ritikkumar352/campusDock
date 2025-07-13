package com.campusDock.controller;

import com.campusDock.dto.MenuItemDto;
import com.campusDock.dto.MenuItemRequestDto;
import com.campusDock.entity.MenuItems;
import com.campusDock.service.ServiceImpl.MenuItemServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;
// Required and Done [Phase -1]
@RestController
@RequestMapping("/api/v1/menuItems")
public class MenuItemsController {

    private final MenuItemServiceImpl menuItemService;
    public MenuItemsController(MenuItemServiceImpl menuItemService) {
        this.menuItemService = menuItemService;
    }


    // 1. Add menu item   -- DONE
    @PostMapping("/canteens/{canteenId}")
    public ResponseEntity<Map<String,String>> addMenuItem(
            @PathVariable UUID canteenId,
            @RequestPart("menuItem") MenuItemRequestDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return menuItemService.addMenuItem(canteenId, dto, file);
    }

    // 2. Get Menu Item   -- DONE
    @GetMapping("/{id}")
    public ResponseEntity<MenuItems> getMenuItemById(@PathVariable("id") UUID id) {
        return menuItemService.getMenuItem(id);
    }

    // 3. get a list of all Menu items of a canteen   -- DONE
    @GetMapping("/canteens/{canteenId}")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsByCanteenId(@PathVariable UUID canteenId) {
        List<MenuItemDto> result=menuItemService.getItemsByCanteenId(canteenId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
