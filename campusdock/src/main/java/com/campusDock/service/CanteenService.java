package com.campusDock.service;


import com.campusDock.dto.CanteenListDto;

import java.util.List;
import java.util.UUID;

public interface CanteenService {


    List<CanteenListDto> getAllCanteens(UUID collegeId);
}