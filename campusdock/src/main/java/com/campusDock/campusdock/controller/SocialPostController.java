package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.dto.AnonFeedPostDto;
import com.campusDock.campusdock.dto.FullPostDto;
import com.campusDock.campusdock.dto.PostDto;
import com.campusDock.campusdock.dto.PostResponseDto;
import com.campusDock.campusdock.entity.SocialPost;
import com.campusDock.campusdock.service.SocialPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/socialPost")
public class SocialPostController {

    private final SocialPostService socialPostService;
    public SocialPostController(SocialPostService socialPostService) {
        this.socialPostService = socialPostService;
    }

    // 0. TODO Set up anon Profile  'AnonUser'



    // 1. Create a Post
    @PostMapping
    public ResponseEntity<?> createSocialPost(@RequestBody PostDto postDto) {
        PostResponseDto response = socialPostService.createSocialPost(postDto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 2. Anon Feed
    // use pagination here .. 10-20 at a time
    @GetMapping
    public ResponseEntity<?> getAllSocialPosts() {
        try {
            List<AnonFeedPostDto> posts = socialPostService.getAllPost();

            if (posts.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No content
            }

            return ResponseEntity.ok(posts);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while retrieving posts. Please try again later.");
        }
    }


    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable UUID postId) {
        try {
            FullPostDto post = socialPostService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}
