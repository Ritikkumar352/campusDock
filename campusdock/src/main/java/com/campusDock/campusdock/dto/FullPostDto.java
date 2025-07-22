package com.campusDock.campusdock.dto;

import com.campusDock.campusdock.entity.Enum.PostType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FullPostDto {
    private UUID id;
    private String content;
    private boolean isAnonymous;
    private PostType postType;
    private String postedBy;  //user name
    private LocalDateTime createdAt;
}
