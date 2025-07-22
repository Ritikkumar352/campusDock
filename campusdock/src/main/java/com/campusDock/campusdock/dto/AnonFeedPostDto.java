package com.campusDock.campusdock.dto;


import com.campusDock.campusdock.entity.Enum.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonFeedPostDto {
    private UUID postId;
    private String content;
    private String postedBy;  // anon UserName
    private PostType postType;
    private LocalDateTime postTime;  // createdAt in Social post
}
