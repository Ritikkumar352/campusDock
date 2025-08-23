package com.campusDock.campusdock.Socials.DTO;
import lombok.Data;
import java.util.UUID;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String imageUrl;
    private UUID authorId;
    private UUID topicId;
    private UUID collegeId;
    private boolean isAnonymous;
    private boolean isPoll;
}
