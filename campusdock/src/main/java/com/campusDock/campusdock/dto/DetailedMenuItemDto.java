package com.campusDock.campusdock.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailedMenuItemDto {
    private UUID id;
    private String foodName;
    private String description;
    private boolean isAvailable;
    private String timeToCook;
    private List<String> urls;

}
