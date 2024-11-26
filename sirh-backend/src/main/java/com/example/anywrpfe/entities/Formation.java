package com.example.anywrpfe.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Formation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "competence_id", nullable = false)
    private Competence targetCompetence;

    private int currentLevel;
    private int targetLevel;

    @Temporal(TemporalType.DATE)
    private Date startingAt;

    @Temporal(TemporalType.DATE)
    private Date finishingAt;


    private double progress; // Percentage progress (e.g., 0.0 to 100.0)

    private int durationInDays; // New field for tracking training duration

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogue_id")
    @JsonBackReference // Prevents circular reference during serialization
    private Catalogue catalogue;

    @OneToMany(mappedBy = "formation")
    private List<Inscription> enrollments; // List of enrollments for this formation
}
