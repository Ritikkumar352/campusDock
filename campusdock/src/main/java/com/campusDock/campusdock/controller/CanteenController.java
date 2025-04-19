package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.entity.DTO.CanteenRequestDto;
import com.campusDock.campusdock.service.ServiceImpl.CanteenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class CanteenController {

    private final CanteenServiceImpl canteenService;

    public CanteenController(CanteenServiceImpl canteenService) {
        this.canteenService = canteenService;
    }

    @GetMapping("/canteenHome")
    public String canteenHome() {
        return "Canteen Home";
    }

    @PostMapping("/registerCanteen")
    public ResponseEntity<Map<String, String>> registerCanteen(
            @RequestPart(value = "canteen", required = false) CanteenRequestDto canteenRequest,
            @RequestPart(value = "media_file", required = false) MultipartFile file
    ) {
        System.out.println("insside canteen register");
//        return canteenService.registerCanteen(canteenRequest, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}