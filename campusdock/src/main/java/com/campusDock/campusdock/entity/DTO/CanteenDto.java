package com.campusDock.campusdock.entity.DTO;

import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
public class CanteenDto {
    private UUID id;
    private String name;
    private String description;
    private boolean isOpen;
    private String createdAt;
    private UUID collegeId;
    private String mediaUrl;
}

