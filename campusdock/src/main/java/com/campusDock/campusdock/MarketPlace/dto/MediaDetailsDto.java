package com.campusDock.campusdock.MarketPlace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaDetailsDto {
    private UUID mediaId;
    private String url;
}