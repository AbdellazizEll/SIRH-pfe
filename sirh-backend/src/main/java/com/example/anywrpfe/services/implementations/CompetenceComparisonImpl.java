package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.dto.CompetenceDetailDTO;
import com.example.anywrpfe.dto.FormationDetailDTO;
import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.repositories.CollaborateurRepository;
import com.example.anywrpfe.repositories.FormationRepository;
import com.example.anywrpfe.services.CompetenceComparisonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompetenceComparisonImpl implements CompetenceComparisonService {
    private final FormationRepository formationRepository;
    private final CollaborateurRepository collaborateurRepository;

    @Override
    public CompetenceComparisonResult compareCollaboratorWithCurrentPoste(Long collaborateurId) {
        // Get the collaborator
        Collaborateur collaborateur = collaborateurRepository.findById(collaborateurId)
                .orElseThrow(() -> new RuntimeException("Collaborateur not found"));

        // Get the position assigned to the collaborator
        Poste poste = collaborateur.getPosteOccupe();
        if (poste == null) {
            throw new RuntimeException("Collaborator does not have an assigned position.");
        }

        // Use the existing comparison method logic
        Map<Long, CollaborateurCompetence> collaborateurCompetences = collaborateur.getCollaborateurCompetences()
                .stream()
                .collect(Collectors.toMap(cc -> cc.getCompetence().getId(), cc -> cc));

        Set<PosteCompetence> posteCompetences = poste.getPosteCompetences();

        CompetenceComparisonResult result = new CompetenceComparisonResult();
        Set<CompetenceDetailDTO> matchingCompetencies = new HashSet<>();
        Set<CompetenceDetailDTO> missingCompetencies = new HashSet<>();
        Set<FormationDetailDTO> suggestedFormations = new HashSet<>();

        for (PosteCompetence pc : posteCompetences) {
            CompetenceDetailDTO dto = new CompetenceDetailDTO();
            dto.setCompetenceId(pc.getCompetence().getId());
            dto.setCompetenceName(pc.getCompetence().getName());
            dto.setPositionEvaluation(pc.getEvaluation());
            dto.setScaleType(pc.getScaleType());

            CollaborateurCompetence cc = collaborateurCompetences.get(pc.getCompetence().getId());

            if (cc != null) {
                int collaboratorEvaluation = convertEvaluationToNumeric(cc.getEvaluation(), cc.getCompetence().getScaleType());
                int positionEvaluation = convertEvaluationToNumeric(pc.getEvaluation(), pc.getCompetence().getScaleType());

                dto.setCollaboratorEvaluation(cc.getEvaluation());
                matchingCompetencies.add(dto);

                // Check if there is a gap between current and required level
                if (collaboratorEvaluation < positionEvaluation) {
                    // Fetch relevant formations (trainings) for this gap
                    List<Formation> relevantFormations = formationRepository
                            .findFormationByTargetCompetenceAndLevelRange(pc.getCompetence().getId(), collaboratorEvaluation, positionEvaluation);

                    for (Formation formation : relevantFormations) {
                        FormationDetailDTO formationDto = FormationDetailDTO.builder()
                                .formationId(formation.getId())
                                .formationTitle(formation.getTitle())
                                .description(formation.getDescription())
                                .targetCompetence(CompetenceDetailDTO.builder()
                                        .competenceId(pc.getCompetence().getId())
                                        .competenceName(pc.getCompetence().getName())
                                        .scaleType(pc.getScaleType())
                                        .positionEvaluation(pc.getEvaluation())
                                        .collaboratorEvaluation(cc.getEvaluation())
                                        .build())
                                .currentLevel(collaboratorEvaluation)
                                .targetLevel(formation.getTargetLevel())
                                .build();

                        suggestedFormations.add(formationDto);
                    }
                }
            } else {
                dto.setCollaboratorEvaluation(null);
                missingCompetencies.add(dto);

                // Fetch relevant formations for missing competence
                int positionEvaluation = convertEvaluationToNumeric(pc.getEvaluation(), pc.getCompetence().getScaleType());
                List<Formation> relevantFormations = formationRepository
                        .findFormationByTargetCompetenceAndLevelRange(pc.getCompetence().getId(), 0, positionEvaluation);

                for (Formation formation : relevantFormations) {
                    FormationDetailDTO formationDto = FormationDetailDTO.builder()
                            .formationId(formation.getId())
                            .formationTitle(formation.getTitle())
                            .description(formation.getDescription())
                            .targetCompetence(CompetenceDetailDTO.builder()
                                    .competenceId(pc.getCompetence().getId())
                                    .competenceName(pc.getCompetence().getName())
                                    .scaleType(pc.getScaleType())
                                    .positionEvaluation(pc.getEvaluation())
                                    .collaboratorEvaluation(null)
                                    .build())
                            .currentLevel(0)
                            .targetLevel(formation.getTargetLevel())
                            .build();

                    suggestedFormations.add(formationDto);
                }
            }
        }

        result.setMatchingCompetencies(matchingCompetencies);
        result.setMissingCompetencies(missingCompetencies);
        result.setSuggestedTrainings(suggestedFormations);

        return result;
    }

    public int convertEvaluationToNumeric(String evaluation, TypeEval scaleType) {
        switch (scaleType) {
            case STARS:
                return switch (evaluation) {
                    case "1 STAR" -> 1;
                    case "2 STARS" -> 2;
                    case "3 STARS" -> 3;
                    case "4 STARS" -> 4;
                    default -> 0;
                };
            case NUMERIC:
                return Integer.parseInt(evaluation);  // Direct numeric evaluation
            case DESCRIPTIF:
                return switch (evaluation) {
                    case "FAIBLE" -> 1;
                    case "MOYEN" -> 2;
                    case "BON" -> 3;
                    case "EXCELLENT" -> 4;
                    default -> 0;
                };
            default:
                throw new IllegalArgumentException("Unknown scale type");
        }
    }

}
