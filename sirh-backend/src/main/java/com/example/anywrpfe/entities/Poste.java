package com.example.anywrpfe.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Poste implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name="idPoste",
            updatable = false)
    private Long idPoste;

    @Column(name="titre_poste",
            nullable = false,
    unique = true)
    private String titre;

    @OneToMany(mappedBy = "poste",  cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<PosteCompetence> posteCompetences;

    @OneToMany(mappedBy = "posteOccupe", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Collaborateur> collaborateurs = new ArrayList<>();
}
