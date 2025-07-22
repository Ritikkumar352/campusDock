package com.campusDock.campusdock.entity;

import com.campusDock.campusdock.entity.Enum.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SocialPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean isAnonymous = true; // true if anon user is shown

    @Enumerated(EnumType.STRING)
    private PostType postType; // CONFESSION, MEME, RANT, etc.

    @ManyToOne
    private User taggedUser; // optional tagging-> ?? wont be exposed ?? .. conside about multiple tag fro diff types of post

    @ManyToOne(optional = false)
    private AnonUser postedBy; // required, even if isAnonymous = false

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean isAcceptedByTaggedUser = false; // only relevant if taggedUser != null
}

