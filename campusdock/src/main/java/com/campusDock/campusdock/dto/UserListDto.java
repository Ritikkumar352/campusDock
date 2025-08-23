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
    private String anonymousName;
    private UserRole role;

    public UserListDto(String anonymousName, String email, UUID id, String name, UserRole role) {
        this.anonymousName = anonymousName;
        this.email = email;
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
