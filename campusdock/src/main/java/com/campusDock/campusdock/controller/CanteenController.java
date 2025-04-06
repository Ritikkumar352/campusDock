package com.campusDock.campusdock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CanteenController {

    @GetMapping("/canteenHome")
    public String canteenHome() {
        return "Canteen Home";
    }

}