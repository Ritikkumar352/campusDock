package com.campusDock.controller;

import com.campusDock.entity.College;
import com.campusDock.dto.CollegeResponseDto;
import com.campusDock.dto.CreateCollegeDto;
import com.campusDock.service.CollegeService;
import com.campusDock.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/colleges")
public class CollegeController {

    private final UserService userService;
    private final CollegeService collegeService;

    public CollegeController(UserService userService, CollegeService collegeService) {
        this.userService = userService;
        this.collegeService = collegeService;
    }


    // **** For test only ******
    @GetMapping("/checking")  // -> EVERYTING -> college-> student -> canteen-> menu-> menuItem-> menu item detail-> media
    public List<College> getDetailedInfo()
    {
        return collegeService.getAll();
    }

    //Getting college be CollegeResponseDto
    @GetMapping
    public List<CollegeResponseDto> getAllCollege()
    {
        return collegeService.getAll().stream()
                .map(CollegeResponseDto::new)
                .collect(Collectors.toList());

    }

    @PostMapping
    public College createCollege(@RequestBody CreateCollegeDto createCollegeDto)
    {
        return collegeService.create(createCollegeDto);
    }
}
