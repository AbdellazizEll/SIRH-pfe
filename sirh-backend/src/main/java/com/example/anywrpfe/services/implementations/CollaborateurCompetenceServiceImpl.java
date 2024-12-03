package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.entities.Enum.typeCompetence;
import com.example.anywrpfe.exception.ApiException;
import com.example.anywrpfe.repositories.*;
import com.example.anywrpfe.services.CollaborateurCompetenceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollaborateurCompetenceServiceImpl implements CollaborateurCompetenceService {
    private final EnrollementRepository enrollementRepository;
    private final DepartementRepository departementRepository;
    private final PosteCompetenceRepository posteCompetenceRepository;

    private  final Logger logger = LoggerFactory.getLogger(CollaborateurCompetenceServiceImpl.class);

    private final CollaborateurRepository collaborateurRepository;
    private final CompetenceRepository competenceRepository;

    private final CollaborateurCompetenceRepository collaborateurCompetenceRepository;
    @Override
    public Collaborateur addCompetenceToCollaborateur(Long collaborateurId, Long competenceId,String evaluation) {
        Collaborateur collaborateur = collaborateurRepository.findById(collaborateurId)
                .orElseThrow(() -> new RuntimeException("Collaborateur not found"));
        Competence competence = competenceRepository.findById(competenceId)
                .orElseThrow(() -> new RuntimeException("Competence not found"));

        // Log the possible values and the evaluation
        logger.info("Possible values for competence {}: {}", competenceId, competence.getPossibleValues());
        logger.info("Evaluation provided: {}", evaluation);
        // Validate evaluation
        if (!competence.getPossibleValues().contains(evaluation)) {
            throw new ApiException("Invalid evaluation for the given competence");
        }


        boolean competenceExists = collaborateur.getCollaborateurCompetences().stream()
                .anyMatch(cc -> cc.getCompetence().getId().equals(competenceId));
        if( competenceExists)
        {
            throw new ApiException("Cannot add the same competence to same collaborator");
        }

        CollaborateurCompetence collaborateurCompetence = new CollaborateurCompetence();
        collaborateurCompetence.setCollaborateur(collaborateur);
        collaborateurCompetence.setCompetence(competence);
        collaborateurCompetence.setEvaluation(evaluation);

        collaborateurCompetence.setScaleType(competence.getScaleType());

        collaborateurCompetenceRepository.save(collaborateurCompetence);
        collaborateur.getCollaborateurCompetences().add(collaborateurCompetence);
        collaborateurRepository.save(collaborateur);

        return collaborateur;
    }

    @Override
    public CollaborateurCompetenceDTO updateCompetenceEvaluation(Long collaborateurId, Long competenceId, String newEvaluation, TypeEval newScaleType) {
        CollaborateurCompetence collaborateurCompetence = collaborateurCompetenceRepository
                .findByCollaborateurIdAndCompetenceId(collaborateurId, competenceId)
                .orElseThrow(() -> new ApiException("Competence not found for this Collaborateur"));

        Competence competence = competenceRepository.findById(competenceId)
                .orElseThrow(() -> new ApiException("Competence not found"));

        List<String> possibleValues = competence.getPossibleValues().stream().toList();

        if (!possibleValues.contains(newEvaluation)) {
            throw new ApiException("Invalid evaluation value for the given competence and scale type");
        }

        logger.info("Possible values for competence {}: {}", competenceId, possibleValues);
        logger.info("Evaluation provided: {}", newEvaluation);

        collaborateurCompetence.setEvaluation(newEvaluation);
        collaborateurCompetenceRepository.save(collaborateurCompetence);

        return CollaborateurCompetenceDTO.fromEntity(collaborateurCompetence);
    }



    @Override
    @Transactional
    public void removeCompetenceFromCollaborateur(Long collaborateurId, Long competenceId) {

      //FETCH THE USER

         Collaborateur user = collaborateurRepository.findById(collaborateurId).orElseThrow(null);

         //FETCH the Assigned competence
        CollaborateurCompetence competenceToRemove = user.getCollaborateurCompetences().stream()
                .filter(cc -> cc.getCompetence().getId().equals(competenceId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Competence not found with id: " + competenceId + " for collaborateur with id: " + collaborateurId));

        user.getCollaborateurCompetences().remove(competenceToRemove);
        collaborateurRepository.save(user);

        collaborateurCompetenceRepository.delete(competenceToRemove);
    }

    @Override
    public List<CollaborateurCompetence> getCompetencesForCollaborateur(Long collaborateurId) {
        return collaborateurCompetenceRepository.findByCollaborateurId(collaborateurId);
    }

    @Override
    public List<CollaborateurCompetence> getCollaborateursForCompetence(Long competenceId) {
        return collaborateurCompetenceRepository.findByCompetenceId(competenceId);
    }

    @Override
    public List<CollaborateurCompetence> getCompetencesByType(Long collaborateurId, TypeEval scaleType) {
        return collaborateurCompetenceRepository.findByCollaborateurIdAndScaleType(collaborateurId, scaleType);
    }

    @Override
    public List<Competence> fetchAvailableCompetence(Long userId) {

        // Fetch current competences of the user
        List<CollaborateurCompetence> currentCompetences = collaborateurCompetenceRepository.findByCollaborateurId(userId);
        logger.info("Current competence {} for user {} ",currentCompetences,userId );

        // Collect IDs of current competences
        Set<Long> currentCompetenceIds = currentCompetences.stream()
                .map(cc -> cc.getCompetence().getId())
                .collect(Collectors.toSet());


        // Fetch all available competences
        List<Competence> allAvailableCompetences = competenceRepository.findAll(); // Assuming this returns List<CompetenceDTO>

        // Filter out competences that the user already has
        List<Competence> filteredCompetences = allAvailableCompetences.stream()
                .filter(competence -> !currentCompetenceIds.contains(competence.getId()))
                .toList();

        log.info("filtered competences",filteredCompetences );


        return filteredCompetences;
    }

    @Override
    public Set<CollaborateurCompetence> fetchCurrentCompetence(Long userId) {
        Collaborateur user = collaborateurRepository.findById(userId).orElseThrow(null);

        // GET THE COMPETENCE ALREADY ASSIGNED TO THE COLLABORATOR
        return user.getCollaborateurCompetences();

    }

    @Override
    @Transactional
    public void initializeCompetenciesForCollaborator(Collaborateur collaborateur) {
        // Fetch all competencies from the repository
        List<Competence> allCompetencies = competenceRepository.findAll();

        if (allCompetencies.isEmpty()) {
            logger.warn("No competencies found in the repository.");
            return; // or throw an exception if this should not happen
        }

        // For each competence, initialize CollaborateurCompetence
        allCompetencies.forEach(competence -> {
            CollaborateurCompetence collaborateurCompetence = new CollaborateurCompetence();
            collaborateurCompetence.setCollaborateur(collaborateur);
            collaborateurCompetence.setCompetence(competence);
            collaborateurCompetence.setEvaluation(getLowestEvaluation(competence));
            collaborateurCompetence.setScaleType(competence.getScaleType());
            collaborateurCompetence.setTypeCompetence(typeCompetence.ACQUISE); // Assuming typeCompetence is an enum or similar
            collaborateur.getCollaborateurCompetences().add(collaborateurCompetence);
        });
    }

    private String getLowestEvaluation(Competence competence) {
        List<String> possibleValues = competence.getPossibleValues();
        if (possibleValues == null || possibleValues.isEmpty()) {
            logger.warn("No possible values found for competence {}", competence.getName());
            return ""; // or throw an exception if this should not happen
        }
        // Assuming the lowest evaluation is the first one
        return possibleValues.get(0);
    }

    //KPIIII

    @Override

    public List<CompetenceCoverageDTO> calculateCompetenceCoverage(Long collaborateurId) {
        List<CollaborateurCompetence> competences = collaborateurCompetenceRepository.getCollaboratorCompetences(collaborateurId);

        Map<Long, CompetenceCoverageDTO> competenceCoverageMap = new HashMap<>();

        for (CollaborateurCompetence cc : competences) {
            int collaboratorEval = cc.getCompetence().convertEvaluationToNumeric(cc.getEvaluation(), cc.getScaleType());
            int positionEval = cc.getCompetence().convertEvaluationToNumeric(cc.getEvaluation(), cc.getScaleType());

            CompetenceCoverageDTO coverageDTO = competenceCoverageMap.getOrDefault(cc.getCompetence().getId(),
                    new CompetenceCoverageDTO(cc.getCompetence().getId(), cc.getCompetence().getName(), 0.0));

            if (collaboratorEval >= positionEval) {
                coverageDTO.setCoveragePercentage(coverageDTO.getCoveragePercentage() + 1);
            }

            competenceCoverageMap.put(cc.getCompetence().getId(), coverageDTO);
        }

        int totalCompetences = competences.size();
        for (CompetenceCoverageDTO dto : competenceCoverageMap.values()) {
            double coveragePercentage = (dto.getCoveragePercentage() / totalCompetences) * 100;
            // Round the coverage percentage to 2 decimal places
            BigDecimal roundedCoverage = BigDecimal.valueOf(coveragePercentage).setScale(2, RoundingMode.HALF_UP);
            dto.setCoveragePercentage(roundedCoverage.doubleValue());
        }

        return new ArrayList<>(competenceCoverageMap.values());
    }


    @Override
    public List<CompetenceGapDTO> getCompetenceGap(Long collaborateurId) {
        List<CompetenceGapDTO> competenceGaps = collaborateurCompetenceRepository.getCompetenceGap(collaborateurId);

        // Calculate gap for each DTO
        for (CompetenceGapDTO dto : competenceGaps) {
            Competence competence = competenceRepository.findById(dto.getCompetenceId()).orElseThrow();
            dto.calculateGap(competence);  // Perform the conversion and calculate the gap
        }

        return competenceGaps;
    }




    @Override
    public List<CompetenceCoverageDTO> calculateCompetenceCoverageByResponsible(Long departmentId) {
        List<Collaborateur> collaborators = collaborateurRepository.findCollaboratorsByDepartmentId(departmentId);

        Map<Long, CompetenceCoverageDTO> competenceCoverageMap = new HashMap<>();

        // Iterate through all collaborators in the department
        for (Collaborateur collaborator : collaborators) {
            for (CollaborateurCompetence cc : collaborator.getCollaborateurCompetences()) {
                int collaboratorEval = cc.getCompetence().convertEvaluationToNumeric(cc.getEvaluation(), cc.getScaleType());
                int positionEval = cc.getCompetence().convertEvaluationToNumeric(cc.getEvaluation(), cc.getScaleType());

                CompetenceCoverageDTO coverageDTO = competenceCoverageMap.getOrDefault(cc.getCompetence().getId(),
                        new CompetenceCoverageDTO(cc.getCompetence().getId(), cc.getCompetence().getName(), 0.0));

                if (collaboratorEval >= positionEval) {
                    coverageDTO.setCoveragePercentage(coverageDTO.getCoveragePercentage() + 1);
                }

                competenceCoverageMap.put(cc.getCompetence().getId(), coverageDTO);
            }
        }

        int totalCollaborators = collaborators.size();
        for (CompetenceCoverageDTO dto : competenceCoverageMap.values()) {
            double coveragePercentage = (dto.getCoveragePercentage() / totalCollaborators) * 100;
            // Round the coverage percentage to 2 decimal places
            BigDecimal roundedCoverage = BigDecimal.valueOf(coveragePercentage).setScale(2, RoundingMode.HALF_UP);
            dto.setCoveragePercentage(roundedCoverage.doubleValue());
        }

        return new ArrayList<>(competenceCoverageMap.values());
    }

    @Override
    public List<CompetenceImprovementRateDTO> calculateCompetenceImprovementRateForAllDepartments() {
        // Fetch all departments
        List<Departement> allDepartments = departementRepository.findAll();

        List<CompetenceImprovementRateDTO> improvementRates = new ArrayList<>();

        // Iterate over all departments to calculate competence improvement rate
        for (Departement department : allDepartments) {
            List<Collaborateur> collaborators = collaborateurRepository.findCollaboratorsByDepartmentId(department.getId_dep());

            double totalImprovementRate = 0.0;
            int numberOfCollaborators = collaborators.size();

            // Calculate competence improvement rate for each collaborator in the department
            for (Collaborateur collaborator : collaborators) {
                for (CollaborateurCompetence cc : collaborator.getCollaborateurCompetences()) {
                    // Using the current evaluation value as an indication of competence level
                    int currentEvalNumeric = cc.getCompetence().convertEvaluationToNumeric(cc.getEvaluation(), cc.getScaleType());
                    totalImprovementRate += currentEvalNumeric;
                }
            }

            // Calculate average improvement rate for the department
            double averageImprovementRate = numberOfCollaborators > 0 ? totalImprovementRate / numberOfCollaborators : 0.0;

            // Round the improvement rate to 2 decimal places
            BigDecimal roundedImprovementRate = BigDecimal.valueOf(averageImprovementRate).setScale(2, RoundingMode.HALF_UP);

            // Create the DTO to store the result
            improvementRates.add(new CompetenceImprovementRateDTO(department.getId_dep(), department.getNomDep(), roundedImprovementRate.doubleValue()));
        }

        return improvementRates;
    }


    @Override
    public List<EnrollmentCountDTO> calculateEnrollmentCountForAllDepartments() {
        // Fetch all departments
        List<Departement> allDepartments = departementRepository.findAll();

        List<EnrollmentCountDTO> enrollmentCounts = new ArrayList<>();

        // Iterate over all departments to calculate enrollment count
        for (Departement department : allDepartments) {
            int totalCompletedEnrollments = 0;

            // Fetch department responsible, if exists
            if (department.getResponsible() != null) {
                totalCompletedEnrollments += enrollementRepository.countByCollaborator_IdAndCompletedTrue(department.getResponsible().getId());
            }

            // Fetch all teams under the department
            for (Equipe equipe : department.getEquipeList()) {
                // Count completed enrollments for each team manager
                if (equipe.getManagerEquipe() != null) {
                    totalCompletedEnrollments += enrollementRepository.countByCollaborator_IdAndCompletedTrue(equipe.getManagerEquipe().getId());
                }

                // Count completed enrollments for all team members
                for (Collaborateur collaborateur : equipe.getCollaborateurs()) {
                    totalCompletedEnrollments += enrollementRepository.countByCollaborator_IdAndCompletedTrue(collaborateur.getId());
                }
            }

            // Create the DTO to store the result
            enrollmentCounts.add(new EnrollmentCountDTO(department.getId_dep(), department.getNomDep(), totalCompletedEnrollments));
        }

        return enrollmentCounts;
    }

    @Override
    public List<DepartmentCompetenceGapDTO> calculateCompetenceGapForAllDepartments() {
        // Fetch all departments
        List<Departement> allDepartments = departementRepository.findAll();
        List<DepartmentCompetenceGapDTO> departmentCompetenceGaps = new ArrayList<>();

        // Iterate over all departments to calculate competence gaps
        for (Departement department : allDepartments) {
            List<Collaborateur> collaborators = collaborateurRepository.findCollaboratorsByDepartmentId(department.getId_dep());
            List<CompetenceGapDTO> competenceGaps = calculateDepartmentCompetenceGaps(collaborators);
            departmentCompetenceGaps.add(new DepartmentCompetenceGapDTO(department.getId_dep(), department.getNomDep(), competenceGaps));
        }

        return departmentCompetenceGaps;
    }

    // Extracted Method 1: Calculate competence gaps for a department
    private List<CompetenceGapDTO> calculateDepartmentCompetenceGaps(List<Collaborateur> collaborators) {
        Map<Long, CompetenceGapDTO> competenceGapMap = new HashMap<>();

        for (Collaborateur collaborator : collaborators) {
            Poste poste = collaborator.getPosteOccupe();
            if (poste != null) {
                calculateCollaboratorCompetenceGaps(collaborator, poste.getPosteCompetences(), competenceGapMap);
            }
        }

        return new ArrayList<>(competenceGapMap.values());
    }

    // Extracted Method 2: Calculate competence gaps for a collaborator
    private void calculateCollaboratorCompetenceGaps(Collaborateur collaborator, Set<PosteCompetence> requiredCompetences, Map<Long, CompetenceGapDTO> competenceGapMap) {
        for (PosteCompetence posteCompetence : requiredCompetences) {
            Competence competence = posteCompetence.getCompetence();
            String requiredLevel = posteCompetence.getEvaluation();

            // Find the collaborator's current level of the competence
            String currentLevel = collaborator.getCollaborateurCompetences().stream()
                    .filter(cc -> cc.getCompetence().getId().equals(competence.getId()))
                    .map(CollaborateurCompetence::getEvaluation)
                    .findFirst()
                    .orElse(null);

            // Update or create the gap entry
            updateCompetenceGap(competenceGapMap, competence, requiredLevel, currentLevel);
        }
    }

    // Extracted Method 3: Update competence gap in the map
    private void updateCompetenceGap(Map<Long, CompetenceGapDTO> competenceGapMap, Competence competence, String requiredLevel, String currentLevel) {
        CompetenceGapDTO gapDTO = competenceGapMap.getOrDefault(competence.getId(),
                new CompetenceGapDTO(competence.getId(), competence.getName(), requiredLevel, currentLevel));

        gapDTO.calculateGap(competence);

        // Add or update if there's a gap
        if (gapDTO.getGap() > 0) {
            gapDTO.incrementNumberOfCollaborators();
            competenceGapMap.put(competence.getId(), gapDTO);
        }
    }


    @Override
    public List<CompetenceGapDTO> getCompetenceGapsForTeam(Long teamId)
    {
        List<Collaborateur> teamCollaborators = collaborateurRepository.findCollaboratorsByTeamIdWithCompetences(teamId);
        Map<Long, CompetenceGapDTO> competenceGapMap = new HashMap<>();

        for (Collaborateur collaborateur : teamCollaborators) {
            List<CompetenceGapDTO> gaps = collaborateurCompetenceRepository.getCompetenceGap(collaborateur.getId());

            for (CompetenceGapDTO dto : gaps) {
                Competence competence = competenceRepository.findById(dto.getCompetenceId()).orElseThrow();
                dto.calculateGap(competence);

                // Check if the competence already exists in the map
                CompetenceGapDTO existingDTO = competenceGapMap.get(dto.getCompetenceId());
                if (existingDTO != null) {
                    // Update gap if necessary (e.g., average, sum, etc.)
                    // Here, we'll keep the maximum gap found
                    if (dto.getGap() > existingDTO.getGap()) {
                        existingDTO.setGap(dto.getGap());
                    }
                    // Increment the number of collaborators with this gap
                    existingDTO.incrementNumberOfCollaborators();
                } else {
                    // Initialize numberOfCollaborators to 1 for the first occurrence
                    dto.incrementNumberOfCollaborators();
                    competenceGapMap.put(dto.getCompetenceId(), dto);
                }
            }
        }

        return new ArrayList<>(competenceGapMap.values());
    }

    @Override
    public List<CompetenceCoverageDTO> calculateCompetenceCoverageByDepartment(Long departmentId)
    {
        List<Collaborateur> collaborators = collaborateurRepository.findCollaboratorsByDepartmentId(departmentId);

        Map<Long, CompetenceCoverageDTO> competenceCoverageMap = new HashMap<>();

        // Iterate through all collaborators in the department
        for (Collaborateur collaborateur : collaborators) {
            for (CollaborateurCompetence cc : collaborateur.getCollaborateurCompetences()) {
                int collaboratorEval = cc.getCompetence().convertEvaluationToNumeric(cc.getEvaluation(), cc.getScaleType());
                int positionEval = cc.getCompetence().convertEvaluationToNumeric(cc.getEvaluation(), cc.getScaleType());

                CompetenceCoverageDTO coverageDTO = competenceCoverageMap.getOrDefault(cc.getCompetence().getId(),
                        new CompetenceCoverageDTO(cc.getCompetence().getId(), cc.getCompetence().getName(), 0.0));

                if (collaboratorEval >= positionEval) {
                    coverageDTO.setCoveragePercentage(coverageDTO.getCoveragePercentage() + 1);
                }

                competenceCoverageMap.put(cc.getCompetence().getId(), coverageDTO);
            }
        }

        int totalCollaborators = collaborators.size();
        for (CompetenceCoverageDTO dto : competenceCoverageMap.values()) {
            double coveragePercentage = (dto.getCoveragePercentage() / totalCollaborators) * 100;
            // Round the coverage percentage to 2 decimal places
            BigDecimal roundedCoverage = BigDecimal.valueOf(coveragePercentage).setScale(2, RoundingMode.HALF_UP);
            dto.setCoveragePercentage(roundedCoverage.doubleValue());
        }

        return new ArrayList<>(competenceCoverageMap.values());
    }

    @Override
    public List<CompetenceCoverageDTO> calculateCompetenceCoverageByTeam(Long teamId) {
        List<Collaborateur> teamCollaborators = collaborateurRepository.findCollaboratorsByTeamId(teamId);
        Map<Long, CompetenceCoverageDTO> competenceCoverageMap = new HashMap<>();

        for (Collaborateur collaborateur : teamCollaborators) {
            // Assuming each collaborateur has a 'posteOccupe' (position) with required competencies
            Poste poste = collaborateur.getPosteOccupe();
            if (poste == null) {
                continue; // Skip if no position assigned
            }

            for (PosteCompetence pc : poste.getPosteCompetences()) {
                Competence competence = pc.getCompetence();
                int requiredEval = competence.convertEvaluationToNumeric(pc.getEvaluation(), pc.getScaleType());

                // Find the collaborateur's competence evaluation
                Optional<CollaborateurCompetence> optionalCC = collaborateur.getCollaborateurCompetences().stream()
                        .filter(cc -> cc.getCompetence().getId().equals(competence.getId()))
                        .findFirst();

                if (optionalCC.isPresent()) {
                    int collaboratorEval = competence.convertEvaluationToNumeric(optionalCC.get().getEvaluation(), optionalCC.get().getScaleType());

                    CompetenceCoverageDTO coverageDTO = competenceCoverageMap.getOrDefault(competence.getId(),
                            new CompetenceCoverageDTO(competence.getId(), competence.getName(), 0.0));

                    if (collaboratorEval >= requiredEval) {
                        coverageDTO.setCoveragePercentage(coverageDTO.getCoveragePercentage() + 1);
                    }

                    competenceCoverageMap.put(competence.getId(), coverageDTO);
                }
            }
        }

        int totalCollaborators = teamCollaborators.size();
        for (CompetenceCoverageDTO dto : competenceCoverageMap.values()) {
            double coveragePercentage = (dto.getCoveragePercentage() / totalCollaborators) * 100;
            BigDecimal roundedCoverage = BigDecimal.valueOf(coveragePercentage).setScale(2, RoundingMode.HALF_UP);
            dto.setCoveragePercentage(roundedCoverage.doubleValue());
        }

        return new ArrayList<>(competenceCoverageMap.values());
    }

}



