package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.dto.CollegeResponseDto;
import com.campusDock.campusdock.dto.CreateCollegeDto;
import com.campusDock.campusdock.service.CollegeService;
import com.campusDock.campusdock.service.UserService;
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

    @GetMapping("/checking")
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
