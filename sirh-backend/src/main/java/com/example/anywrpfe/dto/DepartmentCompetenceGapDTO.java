package com.example.anywrpfe.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentCompetenceGapDTO {
    private Long departmentId;
    private String departmentName;
    private List<CompetenceGapDTO> competenceGaps;

    public DepartmentCompetenceGapDTO(Long departmentId, String departmentName, List<CompetenceGapDTO> competenceGaps) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.competenceGaps = competenceGaps;
    }

}
