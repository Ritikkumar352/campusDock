package com.campusDock.campusdock.Socials.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
@Entity
@Table(name = "poll_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String text;

    private int voteCount;

    @ManyToOne     //bahut saare PollOption....ek poll ke honge
    @JoinColumn(name = "poll_id")
    private Poll poll;
}
