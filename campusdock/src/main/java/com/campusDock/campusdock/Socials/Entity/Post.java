package com.campusDock.campusdock.Socials.Entity;
import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private PostType type; // TEXT, POLL, MEDIA, CONFESSION

    private boolean isAnonymous;

    private LocalDateTime createdAt;

    private int likeCount=0;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

}
