package com.campusDock.repository;

import com.campusDock.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface MediaFileRepo extends JpaRepository<MediaFile, UUID> {
}
