package com.campusDock.campusdock.Socials.Service.Impl;

import com.campusDock.campusdock.Socials.DTO.PostRequest;
import com.campusDock.campusdock.Socials.Entity.Enum.VoteType;
import com.campusDock.campusdock.Socials.Entity.Post;
import com.campusDock.campusdock.Socials.Entity.Topic;
import com.campusDock.campusdock.Socials.Entity.Vote;
import com.campusDock.campusdock.Socials.Repository.PostRepo;
import com.campusDock.campusdock.Socials.Repository.TopicRepo;
import com.campusDock.campusdock.Socials.Repository.VoteRepo;
import com.campusDock.campusdock.Socials.Service.PostService;
import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CollegeRepo;
import com.campusDock.campusdock.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final VoteRepo voteRepo;
    private final TopicRepo topicRepo;
    private final CollegeRepo collegeRepo;


    public PostServiceImpl(PostRepo postRepo, UserRepo userRepo, VoteRepo voteRepo, TopicRepo topicRepo, CollegeRepo collegeRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.voteRepo = voteRepo;
        this.topicRepo = topicRepo;
        this.collegeRepo = collegeRepo;
    }


    @Override
    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    @Override
    public Optional<Post> getPostById(UUID id) {
        return postRepo.findById(id);
    }

    @Override
    @Transactional
    public Post createPost(PostRequest postRequest) {
        User author = userRepo.findById(postRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + postRequest.getAuthorId()));

        College college = collegeRepo.findById(postRequest.getCollegeId())
                .orElseThrow(() -> new RuntimeException("College not found with ID: " + postRequest.getCollegeId()));

        Topic topic = topicRepo.findById(postRequest.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic not found with ID: " + postRequest.getTopicId()));

        Post newPost = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .imageUrl(postRequest.getImageUrl())
                .author(author)
                .topic(topic)
                .college(college)
                .isAnonymous(postRequest.isAnonymous())
                .build();

        if (postRequest.isPoll()) {
            // TODO... create poll and poll options
        }
        return postRepo.save(newPost);
    }

    @Override
    @Transactional
    public void voteOnPost(UUID postId, UUID userId, VoteType voteType) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        VoteType voteEnum = voteType;

        // Check if the user has already voted on this post
        Vote existingVote = voteRepo.findByPostAndUser(post, user);

        if (existingVote != null) {
            if (existingVote.getVoteType() == voteEnum) {
                // Same vote type, so remove the vote (un-vote)
                voteRepo.delete(existingVote);
            } else {
                // Different vote type, so update the existing vote
                existingVote.setVoteType(voteEnum);
                voteRepo.save(existingVote);
            }
        } else {
            // New vote
            Vote newVote = Vote.builder()
                    .post(post)
                    .user(user)
                    .voteType(voteEnum)
                    .build();
            voteRepo.save(newVote);
        }


    }
}
