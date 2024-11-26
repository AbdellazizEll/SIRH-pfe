package com.example.anywrpfe.entities;

import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.entities.Enum.typeCompetence;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CollaborateurCompetence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "collaborateur_id", nullable = false)
    private Collaborateur collaborateur;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "competence_id", nullable = false)
    private Competence competence;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private typeCompetence typeCompetence = com.example.anywrpfe.entities.Enum.typeCompetence.ACQUISE;
    private String evaluation;

    @Enumerated(EnumType.STRING)
    private TypeEval scaleType;
}
