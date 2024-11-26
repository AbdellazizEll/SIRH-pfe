package com.example.anywrpfe.controller.kpis;

import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Inscription;
import com.example.anywrpfe.services.CollaborateurCompetenceService;
import com.example.anywrpfe.services.CompetenceService;
import com.example.anywrpfe.services.EnrollementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/training-kpis")
@RequiredArgsConstructor
@Slf4j
public class FormationKPIController {

    private  final EnrollementService enrollementService;
    private final CollaborateurCompetenceService competenceService;
    @GetMapping("/completion-rate/{collaboratorId}")
    public ResponseEntity<Double> getTrainingCompletionRate(@PathVariable Long collaboratorId) {
        double completionRate = enrollementService.calculateTrainingCompletionRate(collaboratorId);
        return ResponseEntity.ok(completionRate);
    }

    @GetMapping("/average-progress/{collaboratorId}")
    public ResponseEntity<Double> getAverageTrainingProgress(@PathVariable Long collaboratorId) {
        double avgProgress = enrollementService.calculateAverageTrainingProgress(collaboratorId);
        return ResponseEntity.ok(avgProgress);
    }

    @GetMapping("/competence-improvement-rate/{collaboratorId}/{competenceId}")
    public ResponseEntity<Double> getCompetenceImprovementRate(@PathVariable Long collaboratorId, @PathVariable Long competenceId) {
        double improvementRate = enrollementService.calculateCompetenceImprovementRate(collaboratorId, competenceId);
        return ResponseEntity.ok(improvementRate);
    }


    ///////// GENERAL KPIS


    @GetMapping("/enrollment-count/departments")
    public ResponseEntity<List<EnrollmentCountDTO>> getEnrollmentCountByDepartment() {
        List<EnrollmentCountDTO> enrollmentCounts = competenceService.calculateEnrollmentCountForAllDepartments();
        return ResponseEntity.ok(enrollmentCounts);
    }



    @GetMapping("/average-progress")
    public ResponseEntity<Double> getAverageProgress() {
        double averageProgress = enrollementService.getAverageProgress();
        return ResponseEntity.ok(averageProgress);
    }

    @GetMapping("/overall-competence-improvement-rate")
    public ResponseEntity<Double> getOverallCompetenceImprovementRate() {
        double improvementRate = enrollementService.getOverallCompetenceImprovementRate();
        return ResponseEntity.ok(improvementRate);
    }

    @GetMapping("/overall-competence-improvement-rate/by-department")
    public ResponseEntity<List<DepartmentCompetenceImprovementRateDTO>> getOverallCompetenceImprovementRateByDepartment() {
        List<DepartmentCompetenceImprovementRateDTO> improvementRates = enrollementService.getOverallCompetenceImprovementRateByDepartment();
        return ResponseEntity.ok(improvementRates);
    }


    @GetMapping("/assigned-trainings-overview")
    public ResponseEntity<TrainingOverviewDTO> getAssignedTrainingsOverview() {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collaborateur collaborator = (Collaborateur) authentication.getPrincipal();

        // Get the overview from the service
        TrainingOverviewDTO overview = enrollementService.getTrainingOverviewForCollaborator(collaborator.getId());

        return ResponseEntity.ok(overview);
    }



    /// RESPONSIBLE KPIS

    @GetMapping("/training/completion-rate/team/{teamId}")
    public ResponseEntity<Double> getTrainingCompletionRateForTeam(@PathVariable Long teamId) {
        double completionRate = enrollementService.calculateTrainingCompletionRateForTeam(teamId);
        return ResponseEntity.ok(completionRate);
    }



    @GetMapping("/competence-improvement-rate/team/{teamId}")
    public ResponseEntity<List<CompetenceImprovementRateResponsibleDTO>> getCompetenceImprovementRatesForTeam(@PathVariable Long teamId) {
        List<CompetenceImprovementRateResponsibleDTO> improvementRates = enrollementService.calculateCompetenceImprovementRateForTeam(teamId);
        return ResponseEntity.ok(improvementRates);
    }


    @GetMapping("/enrollment-count/team/{teamId}")
    public ResponseEntity<Integer> getEnrollmentCountByTeam(@PathVariable Long teamId) {
        int enrollmentCount = enrollementService.calculateEnrollmentCountForTeam(teamId);
        return ResponseEntity.ok(enrollmentCount);
    }

}
