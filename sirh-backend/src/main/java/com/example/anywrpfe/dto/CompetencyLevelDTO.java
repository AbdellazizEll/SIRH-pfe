package com.example.anywrpfe.dto;


import com.example.anywrpfe.entities.CollaborateurCompetence;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetencyLevelDTO {

    private Long competenceId;
    private String competenceName;
    private String currentLevel;

    public static CompetencyLevelDTO fromEntity(CollaborateurCompetence collaborateurCompetence) {
        return new CompetencyLevelDTO(
                collaborateurCompetence.getCompetence().getId(),
                collaborateurCompetence.getCompetence().getName(),
                collaborateurCompetence.getEvaluation()
        );
    }
}
