package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.service.CanteenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CanteenController {

    private final CanteenService canteenService;
    public CanteenController(CanteenService canteenService) {
        this.canteenService = canteenService;
    }

    @GetMapping("/canteenHome")
    public String canteenHome() {
        return "Canteen Home";
    }

}