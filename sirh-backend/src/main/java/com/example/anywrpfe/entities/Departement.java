package com.example.anywrpfe.entities;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id_dep")
public class Departement  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_dep;
    private String nomDep;

    @OneToMany(mappedBy = "departement", fetch = FetchType.EAGER)
    @JsonManagedReference("department-equipe")
    private List<Equipe> equipeList = new ArrayList<>();

    @OneToOne
    private Collaborateur responsible;

    @OneToMany(mappedBy = "fromDepartment", fetch = FetchType.EAGER)
    @JsonManagedReference("department-demandeAbsence")
    private List<DemandeAbsence> demandeAbsences = new ArrayList<>();


}
