package com.example.anywrpfe.entities;

import com.example.anywrpfe.entities.Enum.EventType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(nullable = false)
    private String description;

}
