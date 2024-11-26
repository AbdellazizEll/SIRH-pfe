package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.*;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EnrollementService {


     Page<EnrollmentDTO> getAllEnrollement(int page, int pageSize);

     EnrollmentDTO addEnrollementToCollaborator(Long demandeFormationId);

    EnrollmentDTO getEnrollementById(Long id);


    @Transactional
    void markTrainingAsCompleted(Long enrollmentId);

    @Transactional
    void evaluateTrainingCompletion(Long enrollmentId, boolean isApproved, String rejection) throws MessagingException;

     void removeEnrollement(Long id);


    @Transactional
    void updateProgressForTraining(Long collaboratorId, Long formationId, int progress);

    Page<EnrollmentDTO> getEnrollmentsByCollaboratorId(Long collaboratorId, int page,int size);


    // KPI
    double calculateTrainingCompletionRate(Long collaboratorId);

    double calculateAverageTrainingProgress(Long collaboratorId);

    double calculateCompetenceImprovementRate(Long collaboratorId, Long competenceId);

    // 1. Total enrollment count
    Long getTotalEnrollmentCount();

    // 2. Completed training count
    Long getTotalCompletedTrainings();

    // 3. Average training progress
    Double getAverageProgress();

    // 4. Overall competence improvement rate
    Double getOverallCompetenceImprovementRate();

    public List<DepartmentCompetenceImprovementRateDTO> getOverallCompetenceImprovementRateByDepartment();

    TrainingOverviewDTO getTrainingOverviewForCollaborator(Long collaboratorId); // New method for the overview


    double calculateTrainingCompletionRateForTeam(Long teamId);

    List<CompetenceImprovementRateResponsibleDTO> calculateCompetenceImprovementRateForTeam(Long teamId);

    int calculateEnrollmentCountForTeam(Long teamId);



}
