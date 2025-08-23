package com.campusDock.campusdock.Socials.Service;

import com.campusDock.campusdock.Socials.DTO.TopicRequest;
import com.campusDock.campusdock.Socials.DTO.TopicResponse;
import com.campusDock.campusdock.Socials.Entity.Topic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopicService {
    List<Topic> getAllTopics();
    Optional<Topic> getTopicById(UUID id);
    Topic createTopic(TopicRequest topicRequest);

    List<TopicResponse> getAllTopicsWithPostCount();
}
