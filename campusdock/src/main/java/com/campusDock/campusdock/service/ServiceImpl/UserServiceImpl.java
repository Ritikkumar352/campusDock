package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.dto.CreateUserDto;
//import com.campusDock.campusdock.entity.DTO.UserResponseDto;
import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CollegeRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final CollegeRepo collegeRepo;

    public UserServiceImpl(UserRepo userRepo, CollegeRepo collegeRepo) {
        this.userRepo = userRepo;
        this.collegeRepo = collegeRepo;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User createUser(CreateUserDto createUserDto) {

        // Extract domain (e.g. abes.ac.in) from email
        String domain = createUserDto.getEmail().substring(createUserDto.getEmail().indexOf("@") + 1);

        College college = collegeRepo.findByDomain(domain)
                .orElseThrow(() -> new RuntimeException("College not registered for domain: " + domain));


        String namee=createUserDto.getEmail().substring(0,createUserDto.getEmail().indexOf("."));

        User user = new User();
        user.setName(namee);
        user.setPassword(createUserDto.getPassword());
        user.setEmail(createUserDto.getEmail());
        user.setCollege(college);
        user.setRole(detectUserRoleFromEmail(createUserDto.getEmail()));

        return userRepo.save(user);


    }

    public UserRole detectUserRoleFromEmail(String email) {
        String localPart = email.substring(0, email.indexOf("@"));        // sayed.22b0121094
        String identifier = localPart.contains(".") ?
                localPart.split("\\.")[1] : "";

        if (identifier.matches(".*\\d.*")) {
            return UserRole.STUDENT; // contains numbers → student roll pattern
        } else if (localPart.contains("admin") || localPart.contains("staff") || localPart.contains("hr")) {
            return UserRole.STAFF;
        } else {
            return UserRole.FACULTY; // default to faculty if it’s non-numeric
        }
    }



}
