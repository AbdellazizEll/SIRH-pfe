package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.CollaborateurCompetence;
import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.Enum.TypeEval;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;

public interface CollaborateurCompetenceService {

    @Transactional
    Collaborateur addCompetenceToCollaborateur(Long collaborateurId, Long competenceId, String newEvaluation);

    @Transactional
    CollaborateurCompetenceDTO updateCompetenceEvaluation(Long collaborateurId, Long competenceId , String newEvaluation , TypeEval newScaleType);



    @Transactional
     void removeCompetenceFromCollaborateur(Long collaborateurId, Long competenceId);

    List<CollaborateurCompetence> getCompetencesForCollaborateur(Long collaborateurId);

    List<CollaborateurCompetence> getCollaborateursForCompetence(Long competenceId);
    List<CollaborateurCompetence> getCompetencesByType(Long collaborateurId, TypeEval scaleType);

     List<Competence> fetchAvailableCompetence(Long userId);

    Set<CollaborateurCompetence> fetchCurrentCompetence(Long userId);

     void initializeCompetenciesForCollaborator(Collaborateur collaborateur);

     ///MANAGER VIEW
    List<CompetenceCoverageDTO> calculateCompetenceCoverage(Long collaborateurId);
    List<CompetenceGapDTO> getCompetenceGap(Long collaborateurId);
     List<CompetenceGapDTO> getCompetenceGapsForTeam(Long teamId);
    /// RH VIEW
     List<CompetenceCoverageDTO> calculateCompetenceCoverageByResponsible(Long departmentId);
     List<CompetenceCoverageDTO> calculateCompetenceCoverageByDepartment(Long departmentId);

     List<CompetenceImprovementRateDTO> calculateCompetenceImprovementRateForAllDepartments() ;
     List<EnrollmentCountDTO> calculateEnrollmentCountForAllDepartments();

     List<DepartmentCompetenceGapDTO> calculateCompetenceGapForAllDepartments();


    List<CompetenceCoverageDTO> calculateCompetenceCoverageByTeam(Long teamId);
}
