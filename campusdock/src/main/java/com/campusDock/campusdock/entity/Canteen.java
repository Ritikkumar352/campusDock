package com.campusDock.campusdock.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "canteens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Canteen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private boolean isOpen;

    @Column(name = "created_at")
    private String createdAt;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "college_id", referencedColumnName = "id")
    @JsonBackReference
    private College college;

    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MenuItems> menuItems;

    // 3. Canteen - Media Files
    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MediaFile> MediaFile;

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<com.campusDock.campusdock.entity.MediaFile> getMediaFile() {
        return MediaFile;
    }

    public void setMediaFile(List<com.campusDock.campusdock.entity.MediaFile> mediaFile) {
        MediaFile = mediaFile;
    }

    public List<MenuItems> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItems> menuItems) {
        this.menuItems = menuItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
