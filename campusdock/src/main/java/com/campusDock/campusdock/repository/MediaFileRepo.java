package com.campusDock.campusdock.repository;

import com.campusDock.campusdock.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Repository
public interface MediaFileRepo extends JpaRepository<MediaFile, UUID> {
}
