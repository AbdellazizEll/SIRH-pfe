package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.auth.exception.formationExceptions.FormationException;
import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.repositories.*;
import com.example.anywrpfe.services.PosteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PosteServiceImpl implements PosteService {

    private final PosteRepository posteRepository;
    private final CollaborateurRepository collaborateurRepository;
    private final FormationRepository formationRepository;
    private final EnrollementRepository enrollementRepository;
    private final DemandeFormationRepository demandeFormationRepository;

    @Override
    public Poste createPoste(PosteDTO posteDTO) {

        Poste poste = new Poste();
        poste.setTitre(posteDTO.getTitre());

        List<Poste> postesList = posteRepository.findAll();

        boolean posteExistant = postesList.stream().anyMatch(pp -> pp.getIdPoste().equals(poste.getTitre()));
        if (posteExistant) {
            throw new FormationException("Cannot add the same poste ");
        }

        return posteRepository.save(poste);
    }

    @Override
    public Optional<PosteDTO> getPosteById(Long id) {
        Optional<Poste> posteOptional = posteRepository.findById(id);

        return posteOptional.map(poste -> {
            Hibernate.initialize(poste.getCollaborateurs());  // Initialize collaborators to avoid lazy loading issues
            return PosteDTO.fromEntity(poste);  // Convert Poste to PosteDTO, which now includes collaborators
        });    }

    @Override
    public PosteDTO updatePoste(Long posteId, PosteDTO posteDTO) {
        // Find the existing poste by its ID
        Optional<Poste> existingPoste = posteRepository.findById(posteId);

        if (existingPoste.isPresent()) {
            Poste posteToUpdate = existingPoste.get();

            // Check if the title is changed and ensure it's unique
            if (!posteToUpdate.getTitre().equals(posteDTO.getTitre())) {
                if (posteRepository.existsByTitre(posteDTO.getTitre())) {
                    throw new FormationException("Veuillez changer le titre s'il vous plait, titre existe déjà");
                }
            }

            // Update the title only
            posteToUpdate.setTitre(posteDTO.getTitre());

            // Save the updated poste
            posteRepository.save(posteToUpdate);

            // Convert to DTO and return
            return PosteDTO.fromEntity(posteToUpdate);

        } else {
            throw new FormationException("Poste not found");
        }
    }
    @Override
    public void removePoste(Long id) {
        Poste poste = posteRepository.findById(id).orElseThrow(() -> new RuntimeException("Poste n'est pas existant"));
        posteRepository.delete(poste);
    }

    @Override
    public List<PosteDTO> findAll() {

        List<Poste> poste =  posteRepository.findAll();

        return poste.stream()
                .map(PosteDTO::fromEntity)
                .collect(Collectors.toList());

    }


    @Override
    public CompetenceComparisonResult compareCollaboratorToPoste(Long collaborateurId, Long posteId) {
        Collaborateur collaborateur = collaborateurRepository.findById(collaborateurId)
                .orElseThrow(() -> new RuntimeException("Collaborateur not found"));
        Poste poste = posteRepository.findById(posteId)
                .orElseThrow(() -> new RuntimeException("Poste not found"));

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

            // Use the conversion function for both collaborator and position evaluations
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
        result.setSuggestedTrainings(suggestedFormations); // Set suggested formations

        return result;
    }

    @Override
    public List<FormationDetailDTO> getFilteredSuggestedTrainings(Long collaborateurId, Long posteId, Long competenceId, Integer currentLevel, Integer targetLevel) {
        // Get the collaborator and poste
        Collaborateur collaborateur = collaborateurRepository.findById(collaborateurId)
                .orElseThrow(() -> new RuntimeException("Collaborateur not found"));
        Poste poste = posteRepository.findById(posteId)
                .orElseThrow(() -> new RuntimeException("Poste not found"));

        // Fetch all competencies of the collaborator mapped by competenceId
        Map<Long, CollaborateurCompetence> collaborateurCompetences = collaborateur.getCollaborateurCompetences()
                .stream()
                .collect(Collectors.toMap(cc -> cc.getCompetence().getId(), cc -> cc));

        // Filter the posteCompetences if a specific competenceId is provided
        Set<PosteCompetence> posteCompetences = poste.getPosteCompetences();
        if (competenceId != null) {
            posteCompetences = posteCompetences.stream()
                    .filter(pc -> pc.getCompetence().getId().equals(competenceId))
                    .collect(Collectors.toSet());
        }

        Set<FormationDetailDTO> suggestedFormations = new HashSet<>();

        for (PosteCompetence pc : posteCompetences) {
            CollaborateurCompetence cc = collaborateurCompetences.get(pc.getCompetence().getId());
            int positionEvaluation = convertEvaluationToNumeric(pc.getEvaluation(), pc.getCompetence().getScaleType());

            if (cc != null) {
                int collaboratorEvaluation = convertEvaluationToNumeric(cc.getEvaluation(), cc.getCompetence().getScaleType());

                // Filter by currentLevel and targetLevel if they are provided
                if ((currentLevel == null || collaboratorEvaluation >= currentLevel) && (targetLevel == null || positionEvaluation <= targetLevel)) {
                    List<Formation> relevantFormations = formationRepository
                            .findFormationByTargetCompetenceAndLevelRange(pc.getCompetence().getId(), collaboratorEvaluation, positionEvaluation);
                    relevantFormations.forEach(formation -> {
                        boolean isEnrolled = enrollementRepository.existsByFormationIdAndCollaboratorId(formation.getId(), collaborateur.getId()); // Check if the collaborator is enrolled
                        suggestedFormations.add(
                                convertToFormationDetailDTO(formation, cc, pc, collaboratorEvaluation, LightCollaboratorDTO.fromEntity(collaborateur), isEnrolled) // Pass isEnrolled flag
                        );
                    });
                }
            } else {
                // Handle missing competence
                if (currentLevel == null || 0 >= currentLevel) {
                    List<Formation> relevantFormations = formationRepository
                            .findFormationByTargetCompetenceAndLevelRange(pc.getCompetence().getId(), 0, positionEvaluation);
                    relevantFormations.forEach(formation -> {
                        boolean isEnrolled = enrollementRepository.existsByFormationIdAndCollaboratorId(formation.getId(), collaborateur.getId()); // Check if the collaborator is enrolled
                        suggestedFormations.add(
                                convertToFormationDetailDTO(formation, null, pc, 0, LightCollaboratorDTO.fromEntity(collaborateur), isEnrolled) // Pass isEnrolled flag
                        );
                    });
                }
            }
        }

        return new ArrayList<>(suggestedFormations);
    }

    @Override
    public void unassignCollaboratorFromPoste(Long posteId, Long collaboratorId) {
        Poste poste = posteRepository.findById(posteId)
                .orElseThrow(() -> new EntityNotFoundException("Poste not found"));
        Collaborateur collaborator = collaborateurRepository.findById(collaboratorId)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        collaborator.setPosteOccupe(null); // Unassign the collaborator
        collaborateurRepository.save(collaborator);
    }

    @Override
    public List<FormationDetailDTO> getTeamSuggestedTrainings(Long managerId, Long competenceId, Integer currentLevel, Integer targetLevel) {
        Collaborateur manager = collaborateurRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Equipe team = manager.getManagerEquipe();
        if (team == null) {
            throw new FormationException("No team found for this manager.");
        }

        Set<Collaborateur> teamCollaborators = team.getCollaborateurs();
        Set<FormationDetailDTO> suggestedFormations = new HashSet<>();

        for (Collaborateur collaborator : teamCollaborators) {
            List<FormationDetailDTO> collaboratorTrainings = getFilteredSuggestedTrainings(
                    collaborator.getId(),
                    collaborator.getPosteOccupe().getIdPoste(),
                    competenceId,
                    currentLevel,
                    targetLevel
            );

            // Only include trainings that have not been requested
            for (FormationDetailDTO training : collaboratorTrainings) {
                boolean alreadyRequested = demandeFormationRepository.existsByFormationIdAndEmployeeId(training.getFormationId(), collaborator.getId());
                if (!alreadyRequested) {
                    suggestedFormations.add(training);  // Only add if not requested
                }
            }
        }

        return new ArrayList<>(suggestedFormations);
    }
    private FormationDetailDTO convertToFormationDetailDTO(Formation formation, CollaborateurCompetence cc, PosteCompetence pc, int collaboratorEvaluation, LightCollaboratorDTO collaboratorDTO, boolean requested) {
        return FormationDetailDTO.builder()
                .formationId(formation.getId())
                .formationTitle(formation.getTitle())
                .description(formation.getDescription())
                .targetCompetence(CompetenceDetailDTO.builder()
                        .competenceId(pc.getCompetence().getId())
                        .competenceName(pc.getCompetence().getName())
                        .scaleType(pc.getScaleType())
                        .positionEvaluation(pc.getEvaluation())
                        .collaboratorEvaluation(cc != null ? cc.getEvaluation() : null)
                        .build())
                .targetedCollaborator(collaboratorDTO)  // Include collaborator details here
                .currentLevel(collaboratorEvaluation)  // Collaborator's current level
                .targetLevel(formation.getTargetLevel())
                .requested(requested)  // Set enrollment status
                .build();
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
