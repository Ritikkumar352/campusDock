package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.CanteenDto;
import com.campusDock.campusdock.dto.CanteenListDto;
import com.campusDock.campusdock.dto.CanteenRequestDto;
import com.campusDock.campusdock.service.ServiceImpl.CanteenServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/colleges")
public class CanteenController {

    private final CanteenServiceImpl canteenService;

    public CanteenController(CanteenServiceImpl canteenService) {
        this.canteenService = canteenService;
    }

    // 1. Register Canteen
    @PostMapping("/{collegeId}/canteens")
    public ResponseEntity<Map<String, String>> registerCanteen(
            @RequestPart(value = "canteen", required = false) CanteenRequestDto canteenRequest,
            @RequestPart(value = "media_file", required = false) MultipartFile file
    ) {
        return canteenService.registerCanteen(canteenRequest, file);
    }

    // 2. Get all canteen by college Id
    @GetMapping("/{collegeId}/canteens")
    public List<CanteenListDto> getAllCanteens(@PathVariable UUID collegeId) {
        return canteenService.getAllCanteens(collegeId);
    }

    // 3. Get a canteen by canteen id
    @GetMapping("/{canteenId}")
    public ResponseEntity<CanteenDto> getCanteenById(@PathVariable("canteenId") UUID canteenId) {
        return canteenService.getCanteenById(canteenId);
    }

    // 4. update open status
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