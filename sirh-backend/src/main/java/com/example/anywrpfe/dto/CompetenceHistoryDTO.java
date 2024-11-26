package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.CompetenceHistory;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data

public class CompetenceHistoryDTO {

    private Long id;
    private String competenceName;

    private LightCollaboratorDTO collaboratorDTO;
    private String oldEvaluation;
    private String newEvaluation;
    private Date changeDate;
    private String changeReason;

    // Map from entity to DTO
    public static CompetenceHistoryDTO fromEntity(CompetenceHistory history) {
        if (history == null) {
            return null;
        }

        return CompetenceHistoryDTO.builder()
                .id(history.getId())
                .competenceName(history.getCompetence().getName())
                .collaboratorDTO(LightCollaboratorDTO.fromEntity(history.getCollaborator()))
                .oldEvaluation(history.getOldEvaluation())
                .newEvaluation(history.getNewEvaluation())
                .changeDate(history.getChangeDate())
                .changeReason(history.getChangeReason())
                .build();
    }


}
