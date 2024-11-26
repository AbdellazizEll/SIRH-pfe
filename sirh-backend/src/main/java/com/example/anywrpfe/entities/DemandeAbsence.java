package com.example.anywrpfe.entities;

import com.example.anywrpfe.entities.Enum.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idDemandeAb")
public class DemandeAbsence implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemandeAb;

    private Date dateDebut;

    private Date datefin;


    @Column(nullable = false)
    private Date isCreatedAt;

    private String comment;

    @Builder.Default
    private Status statusDemande = Status.PENDING;

    @Builder.Default
    private Status approvedByManager = Status.PENDING;
    @Builder.Default
    private Status approvedByResponsableDep = Status.PENDING;
    @Builder.Default
    private Status approvedByRh = Status.PENDING;

    private String justificatifPath;


    @ManyToOne
    @JoinColumn(name = "absence_id")
    private Absence absence;

    @ManyToOne
    private Collaborateur collaborateur;


    private int requestedDays;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="department_id")
    @JsonBackReference("department-demandeAbsence")
    private Departement fromDepartment;
    @ManyToOne
    @JoinColumn(name="equipe_id")
    private Equipe fromEquipe;

    private String refusalReason; // Attribute for refusal reason


    public int calculateRequestedDays() {
        return (int) ChronoUnit.DAYS.between(this.dateDebut.toInstant(), this.datefin.toInstant());
    }
}
