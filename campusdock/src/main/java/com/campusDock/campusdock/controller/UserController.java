package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.CreateUserDto;
import com.campusDock.campusdock.dto.UserListDto;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping  //--> fix this use DTO currently it's returning whole User Entity
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userService.getAllUsers();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        System.out.println("Inside getUserById , User controller");
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    // Get list of all user in a colllege
    public ResponseEntity<List<UserListDto>> getUserList() {
        List<UserListDto> users = userService.getUserList();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserListDto> createUser(
            @RequestBody CreateUserDto createUserDto
    ) {
        return new ResponseEntity<>(userService.createUser(createUserDto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/profile-pic")
    public ResponseEntity<?> uploadProfilePic(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            String profilePicUrl = userService.uploadProfilePic(id, file);
            return ResponseEntity.ok(Map.of(
                    "message", "Profile picture uploaded successfully",
                    "profilePicUrl", profilePicUrl
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload profile picture"));
        }
    }

    @GetMapping("/{id}/profile-pic")
    public ResponseEntity<?> getProfilePic(@PathVariable UUID id) {
        try {
            String profilePicUrl = userService.getProfilePicUrl(id);
            return ResponseEntity.ok(Map.of("profilePicUrl", profilePicUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch profile picture"));
        }
    }


}
