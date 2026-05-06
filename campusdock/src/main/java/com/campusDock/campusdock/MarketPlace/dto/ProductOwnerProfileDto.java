package com.campusDock.campusdock.MarketPlace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOwnerProfileDto {
    private UUID id;
    private String name;
    private String anonymousName;
    private String profilePicUrl;
}
