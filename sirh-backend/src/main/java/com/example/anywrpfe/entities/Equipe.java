package com.example.anywrpfe.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id_equipe")

public class Equipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name="id_equipe",
            updatable = false)
    private Long id_equipe;
    private String nom;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    @JsonBackReference("department-equipe")
    private Departement departement;


    @OneToMany(mappedBy = "equipe",fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Collaborateur> collaborateurs = new HashSet<>();

    @OneToOne
    private Collaborateur managerEquipe;
}
