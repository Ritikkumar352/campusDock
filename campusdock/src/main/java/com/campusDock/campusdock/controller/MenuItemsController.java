package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.service.MenuItemsService;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/colleges")
public class MenuItemsController {

    private final MenuItemsService menuItemsService;
    public MenuItemsController(MenuItemsService menuItemsService) {
        this.menuItemsService = menuItemsService;
    }



}
