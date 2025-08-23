package com.campusDock.campusdock.Socials.Service.Impl;

import com.campusDock.campusdock.Socials.DTO.TopicRequest;
import com.campusDock.campusdock.Socials.DTO.TopicResponse;
import com.campusDock.campusdock.Socials.Entity.Topic;
import com.campusDock.campusdock.Socials.Repository.TopicRepo;
import com.campusDock.campusdock.Socials.Service.TopicService;
import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.repository.CollegeRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TopicServiceimpl implements TopicService {
    private final TopicRepo topicRepo;
    private final CollegeRepo collegeRepo;

    public TopicServiceimpl(TopicRepo topicRepo, CollegeRepo collegeRepo) {
        this.topicRepo = topicRepo;
        this.collegeRepo = collegeRepo;
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicRepo.findAll();
    }

    @Override
    public Optional<Topic> getTopicById(UUID id) {
        return topicRepo.findById(id);
    }

    @Override
    @Transactional
    public Topic createTopic(TopicRequest topicRequest) {
        if (topicRepo.findByName(topicRequest.getName()).isPresent()) {
            throw new RuntimeException("Topic with name '" + topicRequest.getName() + "' already exists.");
        }

        College college = collegeRepo.findById(topicRequest.getCollegeId())
                .orElseThrow(() -> new RuntimeException("College not found with ID: " + topicRequest.getCollegeId()));

        Topic newTopic = Topic.builder()
                .name(topicRequest.getName())
                .description(topicRequest.getDescription())
                .college(college)
                .build();

        return topicRepo.save(newTopic);

    }

    /*
        The query returns a List<Object[]>, where each array contains the Topic entity and the Long post count.
        The service then iterates through this list, extracts the Topic and the count,
        and builds the TopicResponse DTO for each one.
     */
    @Override
    public List<TopicResponse> getAllTopicsWithPostCount() {
        List<Object[]> results = topicRepo.findAllTopicsWithPostCount();
        return results.stream()
                .map(result -> {
                    Topic topic = (Topic) result[0];
                    Long postCount = (Long) result[1];
                    return TopicResponse.builder()
                            .id(topic.getId())
                            .name(topic.getName())
                            .description(topic.getDescription())
                            .postCount(postCount.intValue())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
