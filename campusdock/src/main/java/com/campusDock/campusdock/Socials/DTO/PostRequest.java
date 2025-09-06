package com.campusDock.campusdock.Socials.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isAnonymous")
    private boolean isAnonymous;
    @JsonProperty("isPoll")
    private boolean isPoll;
}
