package com.campusDock.campusdock.service;

import com.campusDock.campusdock.dto.CreateUserDto;
//import com.campusDock.campusdock.entity.DTO.UserResponseDto;
import com.campusDock.campusdock.dto.UserListDto;
import com.campusDock.campusdock.entity.User;
import java.util.List;

public interface UserService {


//    List<User> getAllUsers();

    List<UserListDto> getUserList();


    User createUser(CreateUserDto createUserDto);
}
