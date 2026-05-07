package com.campusDock.campusdock.Socials.Repository;

import com.campusDock.campusdock.Socials.Entity.Post;
import com.campusDock.campusdock.Socials.Entity.Vote;
import com.campusDock.campusdock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface VoteRepo extends JpaRepository<Vote, UUID> {
    Optional<Vote> findByPostAndUser(Post post, User user);
}
