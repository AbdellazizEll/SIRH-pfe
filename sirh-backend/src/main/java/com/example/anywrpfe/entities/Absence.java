package com.example.anywrpfe.entities;

import com.example.anywrpfe.entities.Enum.Motif;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "absence")
public class Absence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long absence;

    @Enumerated(EnumType.STRING)
    private Motif typeAbs;

    @OneToMany(mappedBy = "absence" , fetch = FetchType.EAGER)
    private List<DemandeAbsence> absenceRequests;
}
