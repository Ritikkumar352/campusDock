package com.campusDock.campusdock.repository;

import com.campusDock.campusdock.entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Repository
public interface CanteenRepo extends JpaRepository<Canteen, UUID> {
    List<Canteen> findByCollegeId(UUID collegeId);
}