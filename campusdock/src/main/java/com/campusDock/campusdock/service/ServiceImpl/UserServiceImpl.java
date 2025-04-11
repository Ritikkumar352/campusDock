package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.DTO.CreateUserDto;
import com.campusDock.campusdock.entity.DTO.UserResponseDto;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CollegeRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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

        return userRepo.save(user);


    }



}
