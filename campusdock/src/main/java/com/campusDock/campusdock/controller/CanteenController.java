package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.entity.DTO.CanteenDto;
import com.campusDock.campusdock.entity.DTO.CanteenRequestDto;
import com.campusDock.campusdock.service.ServiceImpl.CanteenServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/canteens")
public class CanteenController {

    private final CanteenServiceImpl canteenService;

    public CanteenController(CanteenServiceImpl canteenService) {
        this.canteenService = canteenService;
    }

    // 1. Register Canteen
    @PostMapping
    public ResponseEntity<Map<String, String>> registerCanteen(
            @RequestPart(value = "canteen", required = false) CanteenRequestDto canteenRequest,
            @RequestPart(value = "media_file", required = false) MultipartFile file
    ) {
        int s= canteenService.registerCanteen(canteenRequest, file).getStatusCodeValue();
        System.out.println(s);
        return null;
    }

    // 2. Get all Canteen in a college
//    @GetMapping("/{coll")
//
//
//    // 2. Get a canteen by canteen id
//    @GetMapping("/{canteenId}")
//    public ResponseEntity<CanteenDto> getCanteenById(@PathVariable("canteenId") UUID canteenId) {
//        return canteenService.getCanteenById(canteenId);
//    }

    // 3. update open status
    @PatchMapping("/{canteenId}/toggle-open")
    public ResponseEntity<CanteenDto> toggleCanteenOpen(@PathVariable("canteenId") UUID canteenId) {
        return null;
    }

    // 4. update canteen details
    @PutMapping("/{canteenId}")
    public ResponseEntity<CanteenDto> updateCanteen(CanteenRequestDto canteenRequestDto) {
        return null;
    }
}