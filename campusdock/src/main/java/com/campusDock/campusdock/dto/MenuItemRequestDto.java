package com.campusDock.campusdock.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class MenuItemRequestDto {
    private String foodName;
    private double price;
    private String description;
    private boolean isAvailable;
    private String timeToCook;
    private UUID canteenId;
}

