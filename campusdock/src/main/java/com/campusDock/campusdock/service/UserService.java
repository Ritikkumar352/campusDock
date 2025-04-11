package com.campusDock.campusdock.service;

import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.DTO.CreateUserDto;
import com.campusDock.campusdock.entity.DTO.UserResponseDto;
import com.campusDock.campusdock.entity.User;
import java.util.List;

public interface UserService {


    List<User> getAllUsers();

    User createUser(CreateUserDto createUserDto);
}
