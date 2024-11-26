package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.auth.exception.formationExceptions.FormationException;
import com.example.anywrpfe.dto.CompetenceImprovementRateResponsibleDTO;
import com.example.anywrpfe.dto.DepartmentCompetenceImprovementRateDTO;
import com.example.anywrpfe.dto.EnrollmentDTO;
import com.example.anywrpfe.dto.TrainingOverviewDTO;
import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.repositories.*;
import com.example.anywrpfe.services.EnrollementService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class EnrollementServiceImpl implements EnrollementService {
    private final CompetenceRepository competenceRepository;
    private final CollaborateurRepository collaborateurRepository;
    private final DepartementRepository departementRepository;

    private final static  String NULL_STRING = "NOT FOUND";

    private final CompetenceHistoryRepository competenceHistoryRepository;
    private final CollaborateurCompetenceRepository collaborateurCompetenceRepository;
    private final DemandeFormationRepository demandeFormationRepository;
    private final EnrollementRepository enrollementRepository;
    @Qualifier("implEmailService")
    private final EmailService emailService;



    @Override
    public Page<EnrollmentDTO> getAllEnrollement(int page, int pageSize) {
        Page<Inscription> enrollementPage = enrollementRepository.findAll(PageRequest.of(page, pageSize));
        return enrollementPage.map(EnrollmentDTO::fromEntity);
    }


    @Override
    @Transactional
    public EnrollmentDTO addEnrollementToCollaborator(Long demandeFormationId) {

        // Fetch the formation request
        DemandeFormation demandeFormation = demandeFormationRepository.findById(demandeFormationId)
                .orElseThrow(() -> new FormationException("Demande Not found"));

        // fetch training from formation request
        Formation training = demandeFormation.getFormation();

        // Check if the collaborator is already enrolled in this formation
        boolean alreadyEnrolled = enrollementRepository.existsByFormationIdAndCollaboratorId(
                training.getId(),
                demandeFormation.getEmployee().getId()
        );
        if (alreadyEnrolled) {
            throw new FormationException("Collaborator is already enrolled in this training.");
        }

        // Proceed to enroll the collaborator
        Inscription enrollment = new Inscription();
        enrollment.setCollaborator(demandeFormation.getEmployee());
        enrollment.setFormation(demandeFormation.getFormation());
        enrollment.setEnrollmentDate(new Date());
        enrollment.setProgress(0); // Starting at 0% progress
        enrollment.setCompleted(false);

        // Save the enrollment and return the DTO
        return EnrollmentDTO.fromEntity(enrollementRepository.save(enrollment));
    }
    @Override
    public EnrollmentDTO getEnrollementById(Long id) {
        Inscription inscription = enrollementRepository.findById(id)
                .orElseThrow(() -> new FormationException("Enrollement not found"));
        return EnrollmentDTO.fromEntity(inscription);
    }

    @Override
    @Transactional
    public void updateProgressForTraining(Long collaboratorId, Long formationId, int progress) {
        // Fetch enrollment record for the given collaborator and formation
        Inscription enrollment = enrollementRepository.findByCollaborator_IdAndFormationId(collaboratorId, formationId);

        if (enrollment == null) {
            throw new FormationException("Enrollment not found for collaboratorId " + collaboratorId + " and formationId " + formationId);
        }

        // Update training progress
        updateTrainingProgress(enrollment.getId(), progress);
    }

    @Transactional
    public void updateTrainingProgress(Long enrollmentId, int progress) {
        Inscription enrollment = enrollementRepository.findById(enrollmentId)
                .orElseThrow(() -> new FormationException(NULL_STRING));

        enrollment.setProgress(progress);

        // If progress reaches 100%, mark it as completed pending evaluation
        if (progress >= 100) {
            // Call the markTrainingAsCompleted method to handle pending evaluation logic
            markTrainingAsCompleted(enrollmentId);
        } else {
            // Save the enrollment if progress is less than 100%
            enrollementRepository.save(enrollment);
        }
    }


    /**
     * Save competence history only if there is a meaningful change in evaluation.
     */
    private void saveCompetenceHistory(Collaborateur collaborator, CollaborateurCompetence competence, String oldEvaluation, String newEvaluation) {
        boolean historyExists = competenceHistoryRepository.existsByCollaboratorAndCompetenceAndOldEvaluationAndNewEvaluation(
                collaborator, competence.getCompetence(), oldEvaluation, newEvaluation);

        if (!historyExists) {
            CompetenceHistory history = new CompetenceHistory();
            history.setCollaborator(collaborator);
            history.setCompetence(competence.getCompetence());
            history.setOldEvaluation(oldEvaluation);
            history.setNewEvaluation(newEvaluation);
            history.setChangeDate(new Date());
            history.setChangeReason("Training completion");

            competenceHistoryRepository.save(history);
        }
    }


    @Override
    public Page<EnrollmentDTO> getEnrollmentsByCollaboratorId(Long collaboratorId, int page, int size) {
        Page<Inscription> enrollementPage = enrollementRepository.findByCollaborator_Id(collaboratorId, PageRequest.of(page, size));
        return enrollementPage.map(EnrollmentDTO::fromEntity);
    }


    @Transactional
    @Override
    public void markTrainingAsCompleted(Long enrollmentId) {
        log.info("Attempting to mark training as completed for enrollmentId: {}", enrollmentId);

        Inscription enrollment = enrollementRepository.findById(enrollmentId)
                .orElseThrow(() -> {
                    log.error("Enrollment not found for id: {}", enrollmentId);
                    return new RuntimeException("Enrollment not found");
                });

        // If progress is 100%, mark the training as pending evaluation
        if (enrollment.getProgress() >= 100) {
            log.info("Enrollment progress is 100%, marking as pending evaluation.");
            enrollment.setPendingEvaluation(true);  // Mark the enrollment as pending evaluation
            enrollment.setCompletionDate(new Date());  // Store the completion date for record keeping
            enrollementRepository.save(enrollment);
            log.info("Enrollment marked as pending evaluation successfully.");
        } else {
            log.error("Enrollment progress is less than 100%. Progress: {}", enrollment.getProgress());
            throw new FormationException("Enrollment progress is less than 100%");
        }
    }


    @Transactional
    @Override
    public void evaluateTrainingCompletion(Long enrollmentId, boolean isApproved, String rejectionReason) throws MessagingException {
        Inscription enrollment = enrollementRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (!enrollment.isPendingEvaluation()) {
            throw new FormationException("Enrollment is not pending evaluation");
        }

        if (isApproved) {
            // Approval logic
            enrollment.setCompleted(true);
            enrollment.setPendingEvaluation(false);
            enrollment.setCompletionDate(new Date());

            Long collaboratorId = enrollment.getCollaborator().getId();
            Long targetCompetence = enrollment.getFormation().getTargetCompetence().getId();

            CollaborateurCompetence collaboratorCompetence = collaborateurCompetenceRepository
                    .findByCollaborateurIdAndCompetenceId(collaboratorId, targetCompetence)
                    .orElseThrow(() -> new RuntimeException("Collaborator Competence not found"));

            String oldEvaluation = collaboratorCompetence.getEvaluation();

            int targetLevel = enrollment.getFormation().getTargetLevel();
            String newEvaluation = collaboratorCompetence.getCompetence()
                    .convertNumericToEvaluation(targetLevel, collaboratorCompetence.getCompetence().getScaleType());

            if (!oldEvaluation.equals(newEvaluation)) {
                collaboratorCompetence.setEvaluation(newEvaluation);
                collaborateurCompetenceRepository.save(collaboratorCompetence);
                saveCompetenceHistory(enrollment.getCollaborator(), collaboratorCompetence, oldEvaluation, newEvaluation);
            }

            // Send approval email notification
            emailService.sendEvaluationResultEmail(
                    enrollment.getCollaborator().getEmail(),
                    enrollment.getCollaborator().getFirstname() + " " + enrollment.getCollaborator().getLastname(),
                    enrollment.getFormation().getTitle(),
                    true,
                    null // No rejection reason for approval
            );

        } else {
            // Rejection logic
            enrollment.setPendingEvaluation(false);
            enrollment.setCompleted(false);
            enrollment.setProgress(80); // Reset progress to indicate retraining required
            enrollment.setEvaluationFeedback(rejectionReason); // Set the rejection reason
            enrollment.setLastEvaluationDate(new Date());

            // Send rejection email notification
            emailService.sendEvaluationResultEmail(
                    enrollment.getCollaborator().getEmail(),
                    enrollment.getCollaborator().getFirstname() + " " + enrollment.getCollaborator().getLastname(),
                    enrollment.getFormation().getTitle(),
                    false,
                    rejectionReason
            );
        }

        enrollementRepository.save(enrollment);
    }


    @Override
    public void removeEnrollement(Long id) {
        Inscription inscription = enrollementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollement not found"));
        enrollementRepository.delete(inscription);
    }

    // KPI
    @Override
    public double calculateTrainingCompletionRate(Long collaboratorId) {
        List<Inscription> enrollments = enrollementRepository.findByCollaborator_Id(collaboratorId);
        long completedTrainings = enrollments.stream().filter(Inscription::isCompleted).count();
        if (enrollments.isEmpty()) {
            return 0;
        }
        return (double) completedTrainings / enrollments.size() * 100;
    }


    @Override
    public double calculateAverageTrainingProgress(Long collaboratorId) {
        List<Inscription> enrollments = enrollementRepository.findByCollaborator_Id(collaboratorId);
        double totalProgress = enrollments.stream().mapToDouble(Inscription::getProgress).sum();
        if (enrollments.isEmpty()) {
            return 0;
        }
        return totalProgress / enrollments.size();
    }

    @Override
    public double calculateCompetenceImprovementRate(Long collaboratorId, Long competenceId) {
        List<CompetenceHistory> histories = competenceHistoryRepository.findByCollaborator_IdAndCompetence_Id(collaboratorId, competenceId);

        if (histories.isEmpty()) {
            return 0;
        }

        CompetenceHistory latest = histories.get(histories.size() - 1); // Latest history entry
        String oldEvaluation = latest.getOldEvaluation();
        String newEvaluation = latest.getNewEvaluation();

        Competence competence = latest.getCompetence();
        TypeEval scaleType = competence.getScaleType();

        // Convert evaluations based on the scale type
        int oldEvaluationNumeric = convertEvaluationToNumeric(oldEvaluation, scaleType);
        int newEvaluationNumeric = convertEvaluationToNumeric(newEvaluation, scaleType);

        if (oldEvaluationNumeric == 0) {
            return 100; // If old evaluation was 0, full improvement
        }
        return ((double) (newEvaluationNumeric - oldEvaluationNumeric) / oldEvaluationNumeric) * 100;
    }

    // Helper method to convert evaluation string to numeric based on scaleType
    private int convertEvaluationToNumeric(String evaluation, TypeEval scaleType) {
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

    @Override
    // 1. Total enrollment count
    public Long getTotalEnrollmentCount() {
        return enrollementRepository.countTotalEnrollments();
    }

    @Override
    // 2. Completed training count
    public Long getTotalCompletedTrainings() {
        return enrollementRepository.countCompletedTrainings();
    }

    @Override
    // 3. Average training progress
    public Double getAverageProgress() {
        Double averageProgress = enrollementRepository.getAverageProgress();
        // Ensure the value is not null and round to 2 decimal places
        return averageProgress != null ? Math.round(averageProgress * 100.0) / 100.0 : 0.0;
    }
    @Override
    public Double getOverallCompetenceImprovementRate() {
        Double improvementRate = enrollementRepository.getOverallCompetenceImprovementRate();
        // Ensure the value is not null and round to 2 decimal places
        return improvementRate != null ? Math.round(improvementRate * 100.0) / 100.0 : 0.0;
    }


    @Override
    public List<DepartmentCompetenceImprovementRateDTO> getOverallCompetenceImprovementRateByDepartment() {
        List<Departement> allDepartments = departementRepository.findAll();

        List<DepartmentCompetenceImprovementRateDTO> improvementRates = new ArrayList<>();

        for (Departement department : allDepartments) {
            Double improvementRate = enrollementRepository.getOverallCompetenceImprovementRateByDepartment(department.getId_dep());
            // Ensure the value is not null and round to 2 decimal places
            double roundedImprovementRate = improvementRate != null ? Math.round(improvementRate * 100.0) / 100.0 : 0.0;

            // Create a DTO for department competence improvement rate
            improvementRates.add(new DepartmentCompetenceImprovementRateDTO(department.getId_dep(), department.getNomDep(), roundedImprovementRate));
        }

        return improvementRates;
    }

    @Override
    public TrainingOverviewDTO getTrainingOverviewForCollaborator(Long collaboratorId) {
        List<Inscription> enrollements = enrollementRepository.findByCollaborator_Id(collaboratorId);

        long completedTrainings = enrollements.stream().filter(Inscription::isCompleted).count();
        long inProgressTrainings = enrollements.stream().filter(enrollment -> !enrollment.isCompleted()).count();

        return new TrainingOverviewDTO(completedTrainings, inProgressTrainings);
    }

    @Override
    public double calculateTrainingCompletionRateForTeam(Long teamId) {
        List<Collaborateur> teamCollaborators = collaborateurRepository.findCollaboratorsByTeamIdWithCompetences(teamId);

        if (teamCollaborators.isEmpty()) {
            return 0.0;
        }

        long totalEnrollments = 0;
        long completedEnrollments = 0;

        for (Collaborateur collaborateur : teamCollaborators) {
            List<Inscription> enrollments = enrollementRepository.findByCollaborator_Id(collaborateur.getId());
            totalEnrollments += enrollments.size();
            completedEnrollments += enrollments.stream().filter(Inscription::isCompleted).count();
        }

        if (totalEnrollments == 0) {
            return 0.0;
        }

        return ((double) completedEnrollments / totalEnrollments) * 100;
    }

    @Override
    public  List<CompetenceImprovementRateResponsibleDTO> calculateCompetenceImprovementRateForTeam(Long teamId) {
        List<Collaborateur> teamCollaborators = collaborateurRepository.findCollaboratorsByTeamIdWithCompetences(teamId);

        if (teamCollaborators.isEmpty()) {
            return Collections.emptyList();
        }

        // Fetch all competences
        List<Competence> allCompetences = competenceRepository.findAll();

        List<CompetenceImprovementRateResponsibleDTO> improvementRates = new ArrayList<>();

        for (Competence competence : allCompetences) {
            double totalImprovementRate = 0.0;
            int count = 0;

            for (Collaborateur collaborateur : teamCollaborators) {
                double improvementRate = calculateCompetenceImprovementRate(collaborateur.getId(), competence.getId());
                totalImprovementRate += improvementRate;
                count++;
            }

            double averageImprovementRate = (count == 0) ? 0.0 : (totalImprovementRate / count);
            // Optionally, round to two decimal places
            averageImprovementRate = Math.round(averageImprovementRate * 100.0) / 100.0;

            CompetenceImprovementRateResponsibleDTO dto = new CompetenceImprovementRateResponsibleDTO(
                    competence.getId(),
                    competence.getName(),
                    averageImprovementRate
            );

            improvementRates.add(dto);
        }

        return improvementRates;
    }

    @Override
    public int calculateEnrollmentCountForTeam(Long teamId) {
        List<Collaborateur> teamCollaborators = collaborateurRepository.findCollaboratorsByTeamIdWithCompetences(teamId);
        int totalCompletedEnrollments = 0;

        for (Collaborateur collaborateur : teamCollaborators) {
            totalCompletedEnrollments += enrollementRepository.countByCollaborator_IdAndCompletedTrue(collaborateur.getId());
        }

        return totalCompletedEnrollments;    }


}

