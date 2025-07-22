package com.campusDock.campusdock.service;


import com.campusDock.campusdock.dto.CanteenDto;
import com.campusDock.campusdock.dto.CanteenListDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CanteenService {


//    ResponseEntity<CanteenDto> getAll();
    List<CanteenListDto> getAllCanteens(UUID collegeId);
}