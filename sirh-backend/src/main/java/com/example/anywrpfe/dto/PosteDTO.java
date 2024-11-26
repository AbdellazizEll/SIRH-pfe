package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Poste;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class PosteDTO {

    private Long idPoste;
    private String titre;
    private Set<PosteCompetenceDTO> requiredCompetence;
    private List<PosteLightCollaboratorDTO> assignedCollaborators;  // New field for collaborators

    public static PosteDTO fromEntity(Poste poste) {
        if (poste == null) {
            return null;
        }

        Hibernate.initialize(poste.getPosteCompetences());


        return PosteDTO.builder()
                .idPoste(poste.getIdPoste())
                .titre(poste.getTitre())
                .requiredCompetence(poste.getPosteCompetences().stream()
                        .map(PosteCompetenceDTO::fromEntity)
                        .collect(Collectors.toSet()))
                .assignedCollaborators(poste.getCollaborateurs().stream()
                        .map(PosteLightCollaboratorDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
