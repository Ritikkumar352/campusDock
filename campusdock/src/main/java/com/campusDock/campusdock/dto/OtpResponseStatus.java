package com.campusDock.campusdock.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpResponseStatus {
    private boolean success;
    private String message;
    private String token;
}
