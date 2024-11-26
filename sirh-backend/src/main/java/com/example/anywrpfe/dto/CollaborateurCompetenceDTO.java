package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.CollaborateurCompetence;
import com.example.anywrpfe.entities.Enum.TypeEval;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollaborateurCompetenceDTO {

    private Long id;
    private String name;
    private String evaluation;
    private TypeEval scaleType;
    private CompetenceDTO competence;
    private LightCollaboratorDTO lightCollaboratorDTO;

    public static CollaborateurCompetenceDTO fromEntity(CollaborateurCompetence collaborateurCompetence) {
        if (collaborateurCompetence == null) {
            return null;
        }

        return CollaborateurCompetenceDTO.builder()
                .id(collaborateurCompetence.getId())
                .name(collaborateurCompetence.getCompetence().getName())
                .evaluation(collaborateurCompetence.getEvaluation())
                .scaleType(collaborateurCompetence.getScaleType())
                .competence(CompetenceDTO.fromEntity(collaborateurCompetence.getCompetence()))
                .lightCollaboratorDTO(LightCollaboratorDTO.fromEntity(collaborateurCompetence.getCollaborateur()))
                .build();
    }
}
