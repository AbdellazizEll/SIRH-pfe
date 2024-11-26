package com.example.anywrpfe.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ComparisonResultDTO {
    private List<String> labels;
    private List<Integer> collaboratorScores;
    private List<Integer> positionScores;
}
