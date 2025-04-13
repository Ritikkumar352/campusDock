package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.service.ServiceImpl.MenuItemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Map;

@RestController("/api/v1/colleges")
public class MenuItemsController {

    private final MenuItemsService menuItemsService;
    public MenuItemsController(MenuItemsService menuItemsService) {
        this.menuItemsService = menuItemsService;
    }





}
