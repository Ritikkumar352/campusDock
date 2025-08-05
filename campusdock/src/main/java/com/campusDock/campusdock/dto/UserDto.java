package com.campusDock.campusdock.dto;
// FOR PRODUct

import com.campusDock.campusdock.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String name;

    // Constructor to map from the User entity
    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}