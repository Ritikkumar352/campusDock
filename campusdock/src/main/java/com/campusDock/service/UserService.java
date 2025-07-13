package com.campusDock.service;

import com.campusDock.dto.CreateUserDto;
//import com.campusDock.campusdock.entity.DTO.UserResponseDto;
import com.campusDock.entity.User;

import java.util.List;

public interface UserService {


    List<User> getAllUsers();

    User createUser(CreateUserDto createUserDto);
}
