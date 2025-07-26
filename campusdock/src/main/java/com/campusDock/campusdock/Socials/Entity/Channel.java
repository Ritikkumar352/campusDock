package com.campusDock.campusdock.Socials.Entity;

import com.campusDock.campusdock.entity.College;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "channels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Channel {       //#HostelTalk, #LostAndFound, #AskSeniors, #MemeZone, #Fests

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "college_id", nullable = false)
    private College college;
}
