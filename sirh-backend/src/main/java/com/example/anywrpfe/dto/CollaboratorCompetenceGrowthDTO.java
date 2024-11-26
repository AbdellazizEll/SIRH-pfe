package com.example.anywrpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollaboratorCompetenceGrowthDTO {
    private LightCollaboratorDTO collaborator;
    private int growth;
}
