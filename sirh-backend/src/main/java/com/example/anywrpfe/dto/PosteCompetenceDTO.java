package com.example.anywrpfe.dto;


import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.entities.PosteCompetence;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PosteCompetenceDTO {

    private Long id;
    private String evaluation;
    private TypeEval scaleType;

    @Builder.Default
    private com.example.anywrpfe.entities.Enum.typeCompetence typeCompetence = com.example.anywrpfe.entities.Enum.typeCompetence.REQUISE;
    private CompetenceDTO  competence;

    public static  PosteCompetenceDTO fromEntity(PosteCompetence posteCompetence) {
        if (posteCompetence == null) {
            return null;
        }

        return PosteCompetenceDTO.builder()
                .id(posteCompetence.getId())
                .evaluation(posteCompetence.getEvaluation())
                .competence(CompetenceDTO.fromEntity(posteCompetence.getCompetence()))
                .typeCompetence(posteCompetence.getTypeCompetence())
                .scaleType(posteCompetence.getScaleType())
                .build();
    }
}
