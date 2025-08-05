package com.campusDock.campusdock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;


    private LocalDateTime listedOn;

    private boolean isServie;
    private String userName;
}