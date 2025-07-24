package com.campusDock.campusdock.dto;

import com.campusDock.campusdock.entity.Enum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDto {
    private UUID id;
    private String name;
    private String email;
    private UserRole role;
}
