package com.campusDock.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CanteenRequestDto {
    private String name;
    private String description;
    private boolean isOpen;
    private UUID college;
    private String createdAt;
}
