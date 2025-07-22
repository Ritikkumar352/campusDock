package com.campusDock.campusdock.service;

import com.campusDock.campusdock.dto.AnonFeedPostDto;
import com.campusDock.campusdock.dto.FullPostDto;
import com.campusDock.campusdock.dto.PostDto;
import com.campusDock.campusdock.dto.PostResponseDto;

import java.util.List;
import java.util.UUID;

public interface SocialPostService {
    // 1.
    PostResponseDto createSocialPost(PostDto postDto);

    List<AnonFeedPostDto> getAllPost();

    FullPostDto getPostById(UUID postId);
}
