package com.campusDock.campusdock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFiles {
    @Id
    @GeneratedValue
    private UUID id;
    private String fileName;
    private String URL;
    private String type;
    private long size;
    private LocalDateTime uploadDate;



    // 2. MenuItem - Media Relation
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuItems menuItems;

    // 3. Canteen - Media Relation
    @ManyToOne
    @JoinColumn(name = "canteen_id")
    private Canteen canteen_media;

    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }

}
