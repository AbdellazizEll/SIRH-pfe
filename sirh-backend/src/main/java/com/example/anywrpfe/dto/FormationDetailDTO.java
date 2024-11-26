package com.example.anywrpfe.dto;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormationDetailDTO {

    private Long formationId;
    private  String formationTitle;
    private String description;
    private CompetenceDetailDTO targetCompetence;
    private LightCollaboratorDTO targetedCollaborator; //added
    private int currentLevel;
    private int targetLevel;

    private boolean requested = false;
}
