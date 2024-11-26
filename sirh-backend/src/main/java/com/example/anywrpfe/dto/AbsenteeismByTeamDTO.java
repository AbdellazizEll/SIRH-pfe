package com.example.anywrpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AbsenteeismByTeamDTO {
    private String teamName;
    private Long totalAbsenceDays;

    public AbsenteeismByTeamDTO(String teamName, Long totalAbsenceDays) {
        this.teamName = teamName;
        this.totalAbsenceDays = totalAbsenceDays;
    }
}
