package com.campusDock.service;

import com.campusDock.entity.College;
import com.campusDock.dto.CreateCollegeDto;

import java.util.List;

public interface CollegeService {
    List<College> getAll();

    College create(CreateCollegeDto createCollegeDto);
}
