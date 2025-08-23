package com.campusDock.campusdock.Socials.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class TopicResponse {
    private UUID id;
    private String name;
    private String description;
    private int postCount;
}