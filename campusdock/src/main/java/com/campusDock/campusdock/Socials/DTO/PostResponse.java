package com.campusDock.campusdock.Socials.DTO;
import com.campusDock.campusdock.Socials.Entity.Comment;
import com.campusDock.campusdock.Socials.Entity.Poll;
import com.campusDock.campusdock.Socials.Entity.Topic;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PostResponse {
    private UUID id;
    private String title;
    private String content;
    private String imageUrl;
    private String authorName; // Will return only the name here, not the full User object
    private String authorAnonymousName;
    private UUID authorId;
    private String topicName; // Will return only the topic name here, not the full Topic object
    @JsonProperty("isAnonymous")
    private boolean isAnonymous;
    private LocalDateTime createdAt;
    private int upvoteCount;
    private int downvoteCount;
    private int commentCount;
}
