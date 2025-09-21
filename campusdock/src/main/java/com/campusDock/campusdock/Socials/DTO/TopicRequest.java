package com.campusDock.campusdock.Socials.DTO;

import lombok.Data;
import java.util.UUID;

@Data
public class TopicRequest {
    private String name;
    private String description;
    private UUID collegeId;
}
