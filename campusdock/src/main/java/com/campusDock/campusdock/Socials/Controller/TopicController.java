package com.campusDock.campusdock.Socials.Controller;
import com.campusDock.campusdock.Socials.DTO.TopicRequest;
import com.campusDock.campusdock.Socials.DTO.TopicResponse;
import com.campusDock.campusdock.Socials.Entity.Topic;
import com.campusDock.campusdock.Socials.Repository.TopicRepo;
import com.campusDock.campusdock.Socials.Service.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public ResponseEntity<List<TopicResponse>> getAllTopics() {
        List<TopicResponse> topicResponses = topicService.getAllTopicsWithPostCount();
        return ResponseEntity.ok(topicResponses);
    }

    @GetMapping("/college/{collegeId}")
    public ResponseEntity<List<TopicResponse>> getAllTopicsByCollegeId(@PathVariable UUID collegeId) {
        List<TopicResponse> topicResponses = topicService.getAllTopicsByCollegeId(collegeId);
        return ResponseEntity.ok(topicResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> getTopicById(@PathVariable UUID id) {
        return topicService.getTopicById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TopicResponse> createTopic(@RequestBody TopicRequest topicRequest) {
        Topic newTopic = topicService.createTopic(topicRequest);
        TopicResponse responseDto = convertToDto(newTopic);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    private TopicResponse convertToDto(Topic topic) {
        int postCount = topic.getPosts() != null ? topic.getPosts().size() : 0;
        return TopicResponse.builder()
                .id(topic.getId())
                .name(topic.getName())
                .description(topic.getDescription())
                .postCount(postCount)
                .build();
    }
}
