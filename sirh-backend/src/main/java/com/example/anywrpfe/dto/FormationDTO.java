package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Formation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class FormationDTO {

    private Long id;
    private String title;
    private String description;
    private CompetenceDTO targetCompetence;
    private int currentLevel;
    private int targetLevel;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startingAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date finishingAt;

    private LightCatalogueDTO catalogue;  // This will map the catalogue DTO instead of causing recursion

    public static FormationDTO fromEntity(Formation formation) {
        if (formation == null) {
            return null;
        }

        return FormationDTO.builder()
                .id(formation.getId())
                .title(formation.getTitle())
                .description(formation.getDescription())
                .targetCompetence(CompetenceDTO.fromEntity(formation.getTargetCompetence()))
                .currentLevel(formation.getCurrentLevel())
                .targetLevel(formation.getTargetLevel())
                .startingAt(formation.getStartingAt())
                .finishingAt(formation.getFinishingAt())
                .catalogue(LightCatalogueDTO.fromEntity(formation.getCatalogue())) // Ensure the catalogue is mapped correctly
                .build();
    }

    public static Formation toEntity(FormationDTO formationDTO) {
        if (formationDTO == null) {
            return null;
        }

        Formation formation = new Formation();
        formation.setId(formationDTO.getId());
        formation.setTitle(formationDTO.getTitle());
        formation.setDescription(formationDTO.getDescription());
        formation.setCurrentLevel(formationDTO.getCurrentLevel());
        formation.setTargetLevel(formationDTO.getTargetLevel());
        formation.setStartingAt(formationDTO.getStartingAt());
        formation.setFinishingAt(formationDTO.getFinishingAt());
        if (formationDTO.getTargetCompetence() != null) {
            formation.setTargetCompetence(CompetenceDTO.toEntity(formationDTO.getTargetCompetence()));
        }

        if (formationDTO.getCatalogue() != null) {
            formation.setCatalogue(LightCatalogueDTO.toEntity(formationDTO.getCatalogue()));
        }
        return formation;
    }
}
