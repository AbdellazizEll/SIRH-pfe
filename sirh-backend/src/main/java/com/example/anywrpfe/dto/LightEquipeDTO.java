package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Equipe;
import lombok.Builder;
import lombok.Data;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class LightEquipeDTO {

    private Long id_equipe;
    private String nom;
    private LightCollaboratorDTO manager;
    private List<LightCollaboratorDTO> subCollaborators;

    public static LightEquipeDTO fromEntity(Equipe equipe) {
        if (equipe == null) {
            return null;
        }

        Hibernate.initialize(equipe.getCollaborateurs());
        return LightEquipeDTO.builder()
                .id_equipe(equipe.getId_equipe())
                .nom(equipe.getNom())
                .manager(LightCollaboratorDTO.fromEntity(equipe.getManagerEquipe()))
                .subCollaborators(
                equipe.getCollaborateurs() != null ?
                        equipe.getCollaborateurs().stream()
                                .map(LightCollaboratorDTO::fromEntity)
                                .collect(Collectors.toList()) : null
        )
                .build();
    }

    public static Equipe toEntity(EquipeDTO dto) {
        if (dto == null) {
            return null;
        }

        Equipe equipe = new Equipe();
        equipe.setId_equipe(dto.getId_equipe());
        equipe.setNom(dto.getNom());

        return equipe;
    }

}
