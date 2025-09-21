package com.campusDock.campusdock.Socials.Entity;

import com.campusDock.campusdock.Socials.Entity.Enum.ActionType;
import com.campusDock.campusdock.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "xp_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XPLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ActionType action;

    private int points;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
