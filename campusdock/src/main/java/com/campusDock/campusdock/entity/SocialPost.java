package com.campusDock.campusdock.entity;

import com.campusDock.campusdock.entity.Enum.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SocialPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String content;

    private boolean isAnonymous = true;

    @Enumerated(EnumType.STRING)
    private PostType postType; // CONFESSION, MEME, RANT, APPRECIATION, ETC

    @ManyToOne
    private User taggedUser;

    @ManyToOne
    private User postedBy;


//    @ManyToOne
//    private AnonUser postedBy;   // use this instead of real user with random username for real user


    private LocalDateTime createdAt;

    private boolean isAcceptedByTaggedUser;  // ? keep it or not


}
