package com.campusDock.controller;

import com.campusDock.dto.CreateUserDto;
import com.campusDock.entity.User;
import com.campusDock.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {

        return new ResponseEntity<>(userService.createUser(createUserDto), HttpStatus.CREATED);
    }

    // create all user of a collge -> get all user by college id
    // get all faculty
    // get all faculty by college id

}
