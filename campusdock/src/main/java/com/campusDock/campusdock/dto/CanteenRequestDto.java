package com.campusDock.campusdock.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CanteenRequestDto {
    private String name;
    private String description;
    private boolean isOpen;
    private UUID college;

    public UUID getCollege() {
        return college;
    }

    public void setCollege(UUID college) {
        this.college = college;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
