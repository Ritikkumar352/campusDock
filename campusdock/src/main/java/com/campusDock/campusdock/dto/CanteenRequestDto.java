package com.campusDock.campusdock.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CanteenRequestDto {
    private String name;
    private String description;
    private boolean isOpen=true;
    private UUID college;
    private String createdAt;
}
