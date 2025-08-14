package com.campusDock.campusdock.Socials.Entity;

import com.campusDock.campusdock.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "polls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String question;

    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne       //bahut saare post ho sate hai....nut user ek hi hoga....therefore ek user bahut saare post bana sqta hai
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PollOption> options;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
