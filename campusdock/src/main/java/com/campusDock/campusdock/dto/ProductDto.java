package com.campusDock.campusdock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String name;
    private String description;
    private BigDecimal price;


    private LocalDateTime listedOn;
    private String userName;
    private UUID userId;    // to navigate to the user profile


}
