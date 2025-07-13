package com.campusDock.service.ServiceImpl;

import com.campusDock.entity.College;
import com.campusDock.dto.CreateCollegeDto;
import com.campusDock.repository.CollegeRepo;
import com.campusDock.service.CollegeService;
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
