package com.campusDock.campusdock.Socials.Repository;

import com.campusDock.campusdock.Socials.Entity.Post;
import com.campusDock.campusdock.Socials.Entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface TopicRepo extends JpaRepository<Topic, UUID> {
    Optional<Topic> findByName(String name);

    @Query("SELECT t, COUNT(p) FROM Topic t LEFT JOIN t.posts p GROUP BY t")
    List<Object[]> findAllTopicsWithPostCount();
}
