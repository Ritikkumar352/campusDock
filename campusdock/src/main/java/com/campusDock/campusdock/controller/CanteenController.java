package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.CanteenDto;
import com.campusDock.campusdock.dto.CanteenListDto;
import com.campusDock.campusdock.dto.CanteenRequestDto;
import com.campusDock.campusdock.service.JwtService;
import com.campusDock.campusdock.service.ServiceImpl.CanteenServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;
// Required and Done [Phase -1]
@RestController
@RequestMapping("/api/v1/colleges")
public class CanteenController {

    private final CanteenServiceImpl canteenService;
    private final JwtService jwtService;

    public CanteenController(CanteenServiceImpl canteenService,JwtService jwtService) {
        this.canteenService = canteenService;
        this.jwtService = jwtService;
    }

    // 1. Register Canteen  -- Done
    @PostMapping("/{collegeId}/canteens")
//    public ResponseEntity<Map<String, String>> registerCanteen(
    public ResponseEntity<?> registerCanteen(
            @RequestPart(value = "canteen", required = false) CanteenRequestDto canteenRequest,
            @RequestPart(value = "media_file", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
//        String role = (String) request.getAttribute("userRole");
        // Get Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        // Extract JWT
        String token = authHeader.substring(7);

        // Extract role from JWT
        String role = jwtService.extractRole(token);
        System.out.println("YOUR ROLE IS :" + role);
        if (role == null || (!role.equals("ADMIN") && !role.equals("SUPER_ADMIN") && !role.equals("CANTEEN_OWNER"))) {
            System.out.println("YOUR ROLE IS :" + role);
            return new ResponseEntity<>("Access Denied: Insufficient role.", HttpStatus.FORBIDDEN);
        }
        return canteenService.registerCanteen(canteenRequest, file);
    }

    // 2. Get all canteen by college Id  -- Done
    /*
      Get list of all canteens in a college with media url (single link)
    */
    @GetMapping("/{collegeId}/canteens")
    public List<CanteenListDto> getAllCanteens(@PathVariable UUID collegeId) {
        return canteenService.getAllCanteens(collegeId);
    }

    // 3. Get a canteen by canteen id --- GET MENU PAGE -> direct
    @GetMapping("/canteens/{canteenId}")
    public ResponseEntity<CanteenDto> getCanteenById(@PathVariable("canteenId") UUID canteenId) {
        return canteenService.getCanteenById(canteenId);
    }







    // DO Later **********




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