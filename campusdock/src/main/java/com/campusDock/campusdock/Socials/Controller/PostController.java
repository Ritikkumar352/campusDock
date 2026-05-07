package com.campusDock.campusdock.Socials.Controller;


import com.campusDock.campusdock.Socials.DTO.PostRequest;
import com.campusDock.campusdock.Socials.DTO.PostResponse;
import com.campusDock.campusdock.Socials.Entity.Enum.VoteType;
import com.campusDock.campusdock.Socials.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> postResponses = postService.getAllPosts();
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/college/{id}")
    public ResponseEntity<List<PostResponse>> getAllPostsByCollegeId(@PathVariable UUID id) {
        List<PostResponse> postResponses = postService.getAllPostsByCollegeId(id);
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        PostResponse responseDto = postService.createPost(postRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/vote")
    public ResponseEntity<?> voteOnPost(@PathVariable UUID postId,
                                        @RequestParam UUID userId,
                                        @RequestParam String voteType) {
        try {
            VoteType vote = VoteType.valueOf(voteType.toUpperCase());
            PostResponse updatedPost = postService.voteOnPost(postId, userId, vote);
            return ResponseEntity.ok(updatedPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid voteType. Use UPVOTE or DOWNVOTE"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("college/{id}/trending")
    public ResponseEntity<List<PostResponse>> getTrendingPostsByCollegeId(@PathVariable UUID id){
        List<PostResponse> postResponses = postService.getTopTrendingPostsByCollegeId(id);
        return ResponseEntity.ok(postResponses);

    }

}
