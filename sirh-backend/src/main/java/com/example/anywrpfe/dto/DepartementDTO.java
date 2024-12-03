package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Departement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class  DepartementDTO {

    private Long id_dep;
    private String nomDep;
    private List<LightEquipeDTO> equipes;
    private LightCollaboratorDTO responsable;

    public static DepartementDTO fromEntity(Departement dep) {
        if (dep == null) {
            return null;
        }
        Hibernate.initialize(dep.getEquipeList());
        return DepartementDTO.builder()
                .id_dep(dep.getId_dep())
                .nomDep(dep.getNomDep())
                .responsable(LightCollaboratorDTO.fromEntity(dep.getResponsible()))
                .equipes(
                        dep.getEquipeList() != null ?
                                dep.getEquipeList().stream()
                                        .map(LightEquipeDTO::fromEntity)
                                        .toList() : null
                )
                .build();

    }
    public static Departement toEntity(DepartementDTO dto) {

        if (dto == null) {
            return null;
        }

        Departement departement = new Departement();
        departement.setId_dep(dto.getId_dep());
        departement.setNomDep(dto.getNomDep());
     //   departement.setResponsible(CollaboratorDTO.toEntity(dto.getResponsible()))

        return departement;
    }

    // Constructor, getters, and setters
}
