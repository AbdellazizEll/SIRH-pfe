package com.example.anywrpfe.entities;

import com.example.anywrpfe.dto.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CompetenceComparisonResult {
    private Set<CompetenceDetailDTO> matchingCompetencies = new HashSet<>();
    private Set<CompetenceDetailDTO> missingCompetencies = new HashSet<>();
    private Set<FormationDetailDTO> suggestedTrainings = new HashSet<>(); // New field
}
