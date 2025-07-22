package com.campusDock.campusdock.dto;

import com.campusDock.campusdock.entity.Enum.PostType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class PostDto {
    private String Content;
    private boolean isAnonymous;
    private PostType type;
    // ? tagged user ?
    private UUID postedBy;  // anonUserId
}
