package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.DTO.CreateCollegeDto;
import com.campusDock.campusdock.repository.CollegeRepo;
import com.campusDock.campusdock.service.CollegeService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CollegeServiceImpl implements CollegeService {

    private final CollegeRepo collegeRepo;


    public CollegeServiceImpl(CollegeRepo collegeRepo) {
        this.collegeRepo = collegeRepo;
    }


    @Override
    public List<College> getAll() {
        return collegeRepo.findAll();
    }

    @Override
    public College create(CreateCollegeDto createCollegeDto) {
        College newCollege=new College();
        newCollege.setName(createCollegeDto.getName());
        newCollege.setDomain(createCollegeDto.getDomain());
        return collegeRepo.save(newCollege);
    }
}
