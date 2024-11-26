package com.example.anywrpfe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompetenceCoverageDTO {
    private Long competenceId;
    private String competenceName;
    private Double coveragePercentage;

    public CompetenceCoverageDTO(Long competenceId, String competenceName, Double coveragePercentage) {
        this.competenceId = competenceId;
        this.competenceName = competenceName;
        this.coveragePercentage = coveragePercentage;
    }


}
