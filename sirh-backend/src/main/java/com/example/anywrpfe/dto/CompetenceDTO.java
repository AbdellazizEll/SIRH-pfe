package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.Enum.TypeEval;
import lombok.Builder;
import lombok.Data;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CompetenceDTO {
    private Long id;
    private String name;
    private String description;
    private TypeEval scaleType;
    private List<String> possibleValues;

    public static CompetenceDTO fromEntity(Competence competence) {
        if (competence == null) {
            return null;
        }
        Hibernate.initialize(competence.getPossibleValues());
        Hibernate.initialize(competence.getCollaborateurCompetences());
        Hibernate.initialize(competence.getPosteCompetences());

        return CompetenceDTO.builder()
                .id(competence.getId())
                .name(competence.getName())
                .description(competence.getDescription())
                .scaleType(competence.getScaleType())
                .possibleValues(new ArrayList<>(competence.getPossibleValues()))
                .build();
    }

    public static Competence toEntity(CompetenceDTO competenceDTO) {
        if (competenceDTO == null) {
            return null;
        }

        Competence competence = new Competence();
        competence.setId(competenceDTO.getId());
        competence.setName(competenceDTO.getName());
        competence.setDescription(competenceDTO.getDescription());
        competence.setScaleType(competenceDTO.getScaleType());

        return competence;
    }
}
