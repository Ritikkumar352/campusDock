package com.campusDock.campusdock.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "media_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String fileName;

    private String url;

    private String type;

    private long size;

    @ManyToOne
    @JoinColumn(name = "canteen_id", referencedColumnName = "id")
    @JsonBackReference
    private Canteen canteen;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    @JsonBackReference
    private MenuItem menuItem;
}
