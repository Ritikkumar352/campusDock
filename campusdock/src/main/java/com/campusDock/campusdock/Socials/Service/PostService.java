package com.campusDock.campusdock.Socials.Service;

import com.campusDock.campusdock.Socials.DTO.PostRequest;
import com.campusDock.campusdock.Socials.DTO.PostResponse;
import com.campusDock.campusdock.Socials.Entity.Enum.VoteType;
import com.campusDock.campusdock.Socials.Entity.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {

    List<PostResponse> getAllPosts();
    Optional<PostResponse> getPostById(UUID id);
    PostResponse createPost(PostRequest postRequest);
    void voteOnPost(UUID postId, UUID userId, VoteType voteType);
    List<PostResponse> getAllPostsByCollegeId(UUID collegeId);
}
