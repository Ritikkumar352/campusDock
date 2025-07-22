package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.AnonFeedPostDto;
import com.campusDock.campusdock.dto.FullPostDto;
import com.campusDock.campusdock.dto.PostDto;
import com.campusDock.campusdock.dto.PostResponseDto;
import com.campusDock.campusdock.entity.AnonUser;
import com.campusDock.campusdock.entity.SocialPost;
import com.campusDock.campusdock.repository.AnonUserRepo;
import com.campusDock.campusdock.repository.SocialPostRepo;
import com.campusDock.campusdock.service.SocialPostService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SocialPostServiceImpl implements SocialPostService {
    private final SocialPostRepo socialPostRepo;
    private final AnonUserRepo anonUserRepo;
    public SocialPostServiceImpl(SocialPostRepo socialPostRepo,AnonUserRepo anonUserRepo) {
        this.socialPostRepo = socialPostRepo;
        this.anonUserRepo = anonUserRepo;
    }
    // 1.
    @Override
    public PostResponseDto createSocialPost(PostDto postDto) {
        try {
            UUID anonUserId = postDto.getPostedBy();
            AnonUser anonUser = anonUserRepo.findById(anonUserId).orElse(null);

            SocialPost socialPost = SocialPost.builder()
                    .content(postDto.getContent())
                    .isAnonymous(postDto.isAnonymous())
                    .postedBy(anonUser)
                    .build();

            SocialPost savedPost = socialPostRepo.save(socialPost);
            return new PostResponseDto(true, "Post created", savedPost.getId());
        } catch (Exception e) {
            System.err.println("Error saving social post: " + e.getMessage());
            return new PostResponseDto(false, "Failed to post, please try again.", null);
        }
    }

    // 2.
    @Override
    public List<AnonFeedPostDto> getAllPost() {
        List<AnonFeedPostDto> anonFeedPostDtos = new ArrayList<>();
        try {
            List<SocialPost> socialPosts = socialPostRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

            for (SocialPost socialPost : socialPosts) {
                AnonFeedPostDto anonFeedPostDto = AnonFeedPostDto.builder()
                        .postId(socialPost.getId())
                        .content(socialPost.getContent())
                        .postType(socialPost.getPostType())
                        .postedBy(socialPost.getPostedBy().getUserName())
                        .postTime(socialPost.getCreatedAt())
                        .build();

                anonFeedPostDtos.add(anonFeedPostDto);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving posts: " + e.getMessage());
            throw new RuntimeException("Unable to fetch social posts at the moment", e);
        }

        return anonFeedPostDtos;
    }

    // 3.
    @Override
    public FullPostDto getPostById(UUID postId) {
        try {
            SocialPost post = socialPostRepo.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

            return FullPostDto.builder()
                    .id(post.getId())
                    .content(post.getContent())
                    .isAnonymous(post.isAnonymous())
                    .postType(post.getPostType())
                    .postedBy(
                            post.isAnonymous() ? "Anonymous" : post.getPostedBy().getUserName()
                    )
                    .createdAt(post.getCreatedAt())
                    .build();

        } catch (Exception e) {
            System.err.println("Error retrieving post: " + e.getMessage());
            throw new RuntimeException("Unable to fetch post details");
        }
    }

}
