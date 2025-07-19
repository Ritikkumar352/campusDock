package com.campusDock.campusdock.dto;


import com.campusDock.campusdock.entity.Enum.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CanteenOwnerRegisterDto {
    private String name;
    private String email;
    private String password;
    private UUID canteenId;

}
