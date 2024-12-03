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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PosteCompetence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "poste_id", nullable = false)
    private Poste poste;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "competence_id", nullable = false)
    private Competence competence;

    @Enumerated(EnumType.STRING)
    private typeCompetence typeCompetence = com.example.anywrpfe.entities.Enum.typeCompetence.REQUISE;

    private String evaluation;

    @Enumerated(EnumType.STRING)
    private TypeEval scaleType;
}
