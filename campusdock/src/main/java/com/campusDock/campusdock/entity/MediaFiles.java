package com.campusDock.campusdock.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
public class MediaFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String fileName;
    private String URL;
    private String type;
    private long size;
    private LocalDateTime uploadDate;



    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }

}
