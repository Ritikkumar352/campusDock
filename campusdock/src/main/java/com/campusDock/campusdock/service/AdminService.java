package com.campusDock.campusdock.service;

import com.campusDock.campusdock.dto.CanteenOwnerRegisterDto;

import java.util.Map;
import java.util.UUID;

public interface AdminService {
    String registerOwner(CanteenOwnerRegisterDto canteenOwner);

    Map<String, String> getCanteenOwner(UUID canteenId);
}
