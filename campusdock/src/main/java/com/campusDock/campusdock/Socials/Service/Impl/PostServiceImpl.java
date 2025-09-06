package com.campusDock.campusdock.Socials.Service.Impl;

import com.campusDock.campusdock.Socials.DTO.PostRequest;
import com.campusDock.campusdock.Socials.DTO.PostResponse;
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
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepo.findAll();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PostResponse> getPostById(UUID id) {
        return postRepo.findById(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public PostResponse  createPost(PostRequest postRequest) {
        log.info("Creating post with isAnonymous: {}", postRequest.isAnonymous());
        
        User author = userRepo.findById(postRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + postRequest.getAuthorId()));

        if (postRequest.isAnonymous() && (author.getAnonymousName() == null || author.getAnonymousName().trim().isEmpty())) {
            author.setAnonymousName(generateAnonymousName());
            userRepo.save(author);
        }
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
        
        log.info("Post built with isAnonymous: {}", newPost.getIsAnonymous());

        if (postRequest.isPoll()) {
            // TODO... create poll and poll options
        }
        Post savedPost = postRepo.save(newPost);
        log.info("Post saved with isAnonymous: {}", savedPost.getIsAnonymous());
        return convertToDto(savedPost);
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

    @Override
    public List<PostResponse> getAllPostsByCollegeId(UUID collegeId) {
        List<Post> posts = postRepo.findByCollegeId(collegeId);
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    private PostResponse convertToDto(Post post) {
        log.info("Converting post to DTO - isAnonymous: {}", post.getIsAnonymous());
        
        int upvotes = (int) (post.getVotes() != null ? post.getVotes().stream().filter(v -> v.getVoteType() == VoteType.UPVOTE).count() : 0);
        int downvotes = (int) (post.getVotes() != null ? post.getVotes().stream().filter(v -> v.getVoteType() == VoteType.DOWNVOTE).count() : 0);

        String authorDisplayName;
        if (post.getIsAnonymous()) {
            String anonymousName = post.getAuthor().getAnonymousName();
            authorDisplayName = (anonymousName != null && !anonymousName.trim().isEmpty())
                    ? anonymousName
                    : "Anonymous";
            log.info("Using anonymous name: {}", authorDisplayName);
        } else {
            authorDisplayName = post.getAuthor().getName();
            log.info("Using real name: {}", authorDisplayName);
        }

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
    private String generateAnonymousName() {
        String[] adjectives = {
                "Jugaadu", "SwagWala", "Mast", "Sanskari", "Bindaas", "Dhaasu",
                "Fadu", "Khatarnak", "Awaara", "Tirchi", "Ghumantu", "Baadshah",
                "Desi", "Chill", "Delulu", "Based", "Goated", "Epic",
                "Turbo", "Quantum", "Cosmic", "Shadow", "Mythic", "Divine",
                "Celestial", "Spicy", "Wobbly", "Zesty", "Whistling", "Blazing",
                "Digital", "Astra", "Vedic",
                "Phantom", "Eternal", "Silent", "Infernal", "Sacred"
        };
        String[] nouns = {
                "Rickshaw", "Autowaala", "Sadhu", "Baba", "Naga", "Naagraj",
                "Shaktimaan", "Hanuman", "Mahakaal", "Ravana", "Arjuna", "Karna",
                "Yodha", "Asura", "Rakshasa", "Thalaivar", "Mogambo", "Basanti",
                "DramaKing", "MemeRaja", "Nimbu", "Gajar", "Launda", "Puchka",
                "PaniPuri", "Jalebi", "VadaPav", "Biskut", "Chai", "Coffee",
                "Robot", "Glitch", "Pixel"
        };
        Random random = new Random();
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        int randomNumber = 100 + random.nextInt(900);
        return adjective + noun + randomNumber;
    }

}
