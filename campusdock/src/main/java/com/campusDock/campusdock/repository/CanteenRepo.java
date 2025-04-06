package com.campusDock.campusdock.repository;

import com.campusDock.campusdock.entity.CanteenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public interface CanteenRepo extends JpaRepository<CanteenEntity, UUID> {

}