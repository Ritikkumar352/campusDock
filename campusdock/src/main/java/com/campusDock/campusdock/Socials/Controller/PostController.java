package com.campusDock.campusdock.Socials.Controller;


import com.campusDock.campusdock.Socials.DTO.PostRequest;
import com.campusDock.campusdock.Socials.DTO.PostResponse;
import com.campusDock.campusdock.Socials.Entity.Enum.VoteType;
import com.campusDock.campusdock.Socials.Entity.Post;
import com.campusDock.campusdock.Socials.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostResponse> postResponses = posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID id) {
        return postService.getPostById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        Post newPost = postService.createPost(postRequest);
        PostResponse responseDto = convertToDto(newPost);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/vote")
    public ResponseEntity<Void> voteOnPost(@PathVariable UUID postId,
                                           @RequestParam UUID userId,
                                           @RequestParam String voteType) {
        try {
            VoteType vote = VoteType.valueOf(voteType.toUpperCase());
            postService.voteOnPost(postId, userId, vote);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private PostResponse convertToDto(Post post) {
//        int upvotes = (int) post.getVotes().stream().filter(v -> v.getVoteType() == VoteType.UPVOTE).count();
//        int downvotes = (int) post.getVotes().stream().filter(v -> v.getVoteType() == VoteType.DOWNVOTE).count();

        int upvotes = (int) (post.getVotes() != null ? post.getVotes().stream().filter(v -> v.getVoteType() == VoteType.UPVOTE).count() : 0);
        int downvotes = (int) (post.getVotes() != null ? post.getVotes().stream().filter(v -> v.getVoteType() == VoteType.DOWNVOTE).count() : 0);

        String authorDisplayName = post.getIsAnonymous() ? post.getAuthor().getAnonymousName() : post.getAuthor().getName();


        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .authorName(authorDisplayName)
                .isAnonymous(post.getIsAnonymous())
                .topicName(post.getTopic().getName())
                .createdAt(post.getCreatedAt())
                .upvoteCount(upvotes)
                .downvoteCount(downvotes)
                .commentCount(post.getComments().size())
                .build();
    }
}
