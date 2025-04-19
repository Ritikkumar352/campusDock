package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.service.ServiceImpl.MenuItemsServiceImpl;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/colleges")
public class MenuItemsController {

    private final MenuItemsServiceImpl menuItemsServiceImpl;
    public MenuItemsController(MenuItemsServiceImpl menuItemsServiceImpl) {
        this.menuItemsServiceImpl = menuItemsServiceImpl;
    }





}
