package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.CanteenOwnerRegisterDto;
import com.campusDock.campusdock.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {


    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 1. Canteen Owner registration
    @PostMapping("/owners")
    public ResponseEntity<?> registerOwner(@RequestBody CanteenOwnerRegisterDto canteenOwner) {
        try {
            String ownerId = adminService.registerOwner(canteenOwner);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Owner registered successfully");
            response.put("ownerId", ownerId);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }



}
