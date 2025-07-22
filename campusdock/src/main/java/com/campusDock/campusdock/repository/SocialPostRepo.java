package com.campusDock.campusdock.repository;

import com.campusDock.campusdock.entity.SocialPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SocialPostRepo extends JpaRepository<SocialPost, UUID> {
}

