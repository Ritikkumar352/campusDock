package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.CanteenDto;
import com.campusDock.campusdock.dto.CanteenListDto;
import com.campusDock.campusdock.dto.CanteenRequestDto;
import com.campusDock.campusdock.service.ServiceImpl.CanteenServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

// Required and Done [Phase -1]
@RestController
@RequestMapping("/api/v1/colleges")
public class CanteenController {

    private final CanteenServiceImpl canteenService;

    public CanteenController(CanteenServiceImpl canteenService) {
        this.canteenService = canteenService;
    }

    // 1. Register Canteen  -- Done
    @PostMapping("/{collegeId}/canteens")
//    public ResponseEntity<Map<String, String>> registerCanteen(
    public ResponseEntity<?> registerCanteen(
            @RequestPart(value = "canteen", required = false) CanteenRequestDto canteenRequest,
            @RequestPart(value = "media_file", required = false) MultipartFile file
    ) {
        return canteenService.registerCanteen(canteenRequest, file);
    }

    // 2. Get all canteen by college Id  -- Done

    /*
        -- It's for EVERYONE -- OPEN
      Get list of all canteens in a college with media url (single link)
    */
    @GetMapping("/{collegeId}/canteens")
    public List<CanteenListDto> getAllCanteens(@PathVariable UUID collegeId) {
        return canteenService.getAllCanteens(collegeId);
    }

    // 3. Get a canteen by canteen id --- GET MENU PAGE -> direct
    //     -- It's for EVERYONE -- OPEN
    @GetMapping("/canteens/{canteenId}")
    public ResponseEntity<CanteenDto> getCanteenById(@PathVariable("canteenId") UUID canteenId) {
        return canteenService.getCanteenById(canteenId);
    }


    // DO Later **********


    // 4. update open status
    // Not allowed if not Admin or Super Admin OR Canteen Owner
    @PatchMapping("/{canteenId}/toggle-open")
    public ResponseEntity<CanteenDto> toggleCanteenOpen(
            @PathVariable("canteenId") UUID canteenId
    ) {
        // TODO :- implement this
        return null;
    }

    // 4. update canteen details
    // Not allowed if not Admin or Super Admin OR Canteen Owner
    @PutMapping("/{canteenId}")
    public ResponseEntity<CanteenDto> updateCanteen(
            CanteenRequestDto canteenRequestDto
    ) {
        // TODO :- implement this
        return null;
    }
}