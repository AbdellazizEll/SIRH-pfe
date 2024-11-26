package com.example.anywrpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollaboratorSuggestTrainingsDTO {

    private Long formationId;
    private String formationTitle;
    private String description;
    private int currentLevel;
    private int targetLevel;

    private CollaboratorDTO collaborator; // New field for collaborator details
    private CompetenceDTO targetCompetence; // Details of the target competence

}
