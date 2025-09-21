package com.campusDock.campusdock.Socials.Repository;

import com.campusDock.campusdock.Socials.Entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface PostRepo extends JpaRepository<Post, UUID> {
    List<Post> findByCollegeId(UUID id);
    List<Post> findByTopicId(UUID topicId);

    // Custom query to fetch all posts and eagerly load the author and topic
    @Query("SELECT p FROM Post p JOIN FETCH p.author a JOIN FETCH p.topic t")
    List<Post> findAllWithAuthorAndTopic();

    // Custom query to fetch a single post by ID and eagerly load the author and topic
    @Query("SELECT p FROM Post p JOIN FETCH p.author a JOIN FETCH p.topic t WHERE p.id = :id")
    Optional<Post> findByIdWithAuthorAndTopic(@Param("id") UUID id);

    @Query("SELECT p FROM Post p LEFT JOIN p.votes v WHERE p.college.id = :collegeId GROUP BY p.id ORDER BY COUNT(v) DESC")
    List<Post> findTrendingByCollegeId(UUID collegeId, Pageable pageable);


}
