package com.example.anywrpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetenceImprovementRateDTO {
    private Long departmentId;
    private String departmentName;
    private double improvementRate;
}
