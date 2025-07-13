package com.campusDock.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanteenListDto {
    private UUID id;

    private String name;

    private boolean isOpen;

    // later add 1 photo of canteen
}
