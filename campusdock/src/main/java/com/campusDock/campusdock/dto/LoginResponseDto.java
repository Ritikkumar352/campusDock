package com.campusDock.campusdock.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private boolean success;
    private String message;
    private String token;
    private String role;
    private String userId;
}
