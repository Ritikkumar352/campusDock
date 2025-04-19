package com.campusDock.campusdock.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {
    private UUID id;
    private String name;
    private String price;
    private boolean is_available;


}
