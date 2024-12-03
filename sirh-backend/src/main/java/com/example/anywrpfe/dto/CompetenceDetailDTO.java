package com.example.anywrpfe.dto;


import com.example.anywrpfe.entities.Enum.TypeEval;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CompetenceDetailDTO {
    private Long competenceId;
    private String competenceName;
    private String positionEvaluation;
    private String collaboratorEvaluation;
    private TypeEval scaleType;

}
