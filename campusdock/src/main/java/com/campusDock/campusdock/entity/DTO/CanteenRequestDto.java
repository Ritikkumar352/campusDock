package com.campusDock.campusdock.entity.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CanteenRequestDto {
    private String name;
    private String description;
    private boolean is_open;
    private UUID college_id;
    private String created_at;
}
