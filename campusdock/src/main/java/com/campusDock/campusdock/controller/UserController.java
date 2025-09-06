package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.CreateUserDto;
import com.campusDock.campusdock.dto.UserListDto;
import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.service.UserService;
import com.campusDock.campusdock.util.RoleValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final RoleValidator roleValidator;

    public UserController(UserService userService, RoleValidator roleValidator) {
        this.userService = userService;
        this.roleValidator = roleValidator;
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
    public ResponseEntity<List<UserListDto>> getUserList(
            HttpServletRequest request
    ) {
        // Not allowed if not Admin or Super Admin
        if (!roleValidator.hasAccess(request,
                UserRole.SUPER_ADMIN,
                UserRole.ADMIN)
        ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<UserListDto> users = userService.getUserList();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserListDto> createUser(
            @RequestBody CreateUserDto createUserDto,
            HttpServletRequest request
    ) {
        // Not allowed if not Admin or Super Admin
        if (!roleValidator.hasAccess(request,
                UserRole.SUPER_ADMIN,
                UserRole.ADMIN)
        ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return new ResponseEntity<>(userService.createUser(createUserDto), HttpStatus.CREATED);
    }


}
