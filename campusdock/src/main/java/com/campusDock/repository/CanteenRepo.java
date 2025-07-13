package com.campusDock.repository;

import com.campusDock.entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CanteenRepo extends JpaRepository<Canteen, UUID> {
    List<Canteen> findByCollegeId(UUID collegeId);
}