package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.UserListDto;
import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.dto.CreateUserDto;
//import com.campusDock.campusdock.entity.DTO.UserResponseDto;
import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CollegeRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final CollegeRepo collegeRepo;

    public UserServiceImpl(UserRepo userRepo, CollegeRepo collegeRepo) {
        this.userRepo = userRepo;
        this.collegeRepo = collegeRepo;
    }

//    @Override
//    public List<User> getAllUsers() {
//        return userRepo.findAll();
//    }

    public List<UserListDto> getUserList() {
        List<User> users = userRepo.findAll();
        List<UserListDto> userListDtos = new ArrayList<>();

        for (User user : users) {
            try {
                if (user != null && user.getEmail() != null) { // simple validation
                    UserListDto dto = new UserListDto(user.getId(), user.getName(), user.getEmail(),user.getRole());
                    userListDtos.add(dto);
                }
            } catch (Exception e) {
                System.err.println("Failed to convert user: " + user.getId());
            }
        }

        return userListDtos;
    }





    @Override
    public UserListDto createUser(CreateUserDto createUserDto) {

        String email= createUserDto.getEmail();
        String domain = email.substring(email.indexOf("@") + 1);
        College college = collegeRepo.findByDomain(domain)
                .orElseThrow(() -> new RuntimeException("College not registered for domain: " + domain));

        String user_name = email.substring(0, email.indexOf("."));

        User user = new User();
        user.setName(user_name);
        user.setPassword("otp-login"); // TODO: Handle securely later
        user.setEmail(email);
        user.setCollege(college);

        // Determine role based on second part of email
        try {
            String afterFirstDot = email.substring(email.indexOf('.') + 1, email.indexOf('@'));
            if (afterFirstDot.matches(".*\\d.*")) {
                user.setRole(UserRole.STUDENT);
            } else {
                user.setRole(UserRole.FACULTY);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid email format: " + email);
        }

        userRepo.save(user);

        return UserListDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();



    }



}
