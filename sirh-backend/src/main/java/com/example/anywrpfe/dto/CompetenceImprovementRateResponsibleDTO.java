package com.example.anywrpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetenceImprovementRateResponsibleDTO {

    private Long competenceId;
    private String competenceName;
    private double improvementRate; // Percentage improvement rate
}
