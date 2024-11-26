package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Equipe;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class EquipeDTO {

    private Long id_equipe;
    private String nom;
    private DepartementDTO fromDepartment;
   private List<LightCollaboratorDTO> collaborateurs;
    private LightCollaboratorDTO managerEquipe;

    // Constructor, getters, and setters


    public static EquipeDTO fromEntity(Equipe equipe) {
        if (equipe == null) {
            return null;
        }

        return EquipeDTO.builder()
                .id_equipe(equipe.getId_equipe())
                .nom(equipe.getNom())
                .managerEquipe(LightCollaboratorDTO.fromEntity(equipe.getManagerEquipe()))
               .fromDepartment(DepartementDTO.fromEntity(equipe.getDepartement()))
                .collaborateurs(equipe.getCollaborateurs() != null ?
                                equipe.getCollaborateurs().stream()
                        .map(LightCollaboratorDTO::fromEntity)
                        .collect(Collectors.toList()): null)

                .build();
    }
    public static Equipe toEntity(EquipeDTO dto) {
        if(dto == null )
        {
            return null ;
        }

        Equipe equipe = new Equipe();
        equipe.setId_equipe(dto.getId_equipe());
        equipe.setNom(dto.getNom());

        return equipe;
    }
}
