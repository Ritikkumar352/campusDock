package com.campusDock.campusdock.service;

import com.campusDock.campusdock.dto.CollegeNameAndDomainDto;
import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.dto.CreateCollegeDto;

import java.util.List;

public interface CollegeService {
    List<College> getAll();

    College create(CreateCollegeDto createCollegeDto);

    List<CollegeNameAndDomainDto> getAllCollegeName();
}
