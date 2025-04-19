package com.campusDock.campusdock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "canteen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Canteen {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    @Column(name = "canteen_desc", nullable = true)
    private String description;
    private boolean is_open = true;  // default open
    private UUID college_id;
    private String created_at;  // Canteen opened date

    // 1. Canteen menu Relation
    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL)
    private List<MenuItems> menu_items;

    // 2. Canteen Media relation
    @OneToMany(mappedBy = "canteen_media", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFiles> media_files;
}

