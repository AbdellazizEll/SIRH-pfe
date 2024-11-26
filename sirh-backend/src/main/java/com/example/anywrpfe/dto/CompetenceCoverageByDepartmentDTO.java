package com.example.anywrpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompetenceCoverageByDepartmentDTO {

    private String departmentName;
    private double competenceCoveragePercentage;
}
