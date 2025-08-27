package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.DetailedMenuItemDto;
import com.campusDock.campusdock.dto.MenuItemDto;
import com.campusDock.campusdock.dto.MenuItemRequestDto;
import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.service.ServiceImpl.MenuItemServiceImpl;
import com.campusDock.campusdock.util.RoleValidator;
import jakarta.servlet.http.HttpServletRequest;
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
    private final RoleValidator roleValidator;

    public MenuItemsController(MenuItemServiceImpl menuItemService, RoleValidator roleValidator) {
        this.roleValidator = roleValidator;
        this.menuItemService = menuItemService;
    }


    // 1. Add menu item   -- DONE
    @PostMapping("/canteens/{canteenId}")
    public ResponseEntity<Map<String, Object>> addMenuItem(
            @PathVariable UUID canteenId,
            @RequestPart("menuItem") MenuItemRequestDto dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            HttpServletRequest request
    ) {
        // Not allowed if not Admin or Super Admin
        if (!roleValidator.hasAccess(request,
                UserRole.SUPER_ADMIN,
                UserRole.ADMIN)
        ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return menuItemService.addMenuItem(canteenId, dto, files);
    }

    // 2. get a list of all Menu items of a canteen   -- DONE
    @GetMapping("/canteens/{canteenId}")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsByCanteenId(@PathVariable UUID canteenId) {
        List<MenuItemDto> result = menuItemService.getItemsByCanteenId(canteenId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 3. Get Menu Item   -- DONE
    @GetMapping("/{id}")
    public ResponseEntity<DetailedMenuItemDto> getMenuItemById(@PathVariable UUID id) {
        DetailedMenuItemDto dto = menuItemService.getMenuItem(id);
        return ResponseEntity.ok(dto);
    }


}
