package com.campusDock.repository;


import com.campusDock.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollegeRepo extends JpaRepository<College, UUID>
{
    public Optional<College> findByDomain(String domain);
}
