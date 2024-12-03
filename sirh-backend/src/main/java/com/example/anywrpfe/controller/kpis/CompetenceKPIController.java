package com.example.anywrpfe.controller.kpis;

import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.CompetenceComparisonResult;
import com.example.anywrpfe.services.CollaborateurCompetenceService;
import com.example.anywrpfe.services.CompetenceComparisonService;
import com.example.anywrpfe.services.EnrollementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/competence-kpis")
@RequiredArgsConstructor
@Slf4j
public class CompetenceKPIController {

    private final CollaborateurCompetenceService competenceService;

    private final EnrollementService enrollementService;

    private final CompetenceComparisonService competenceComparisonService;

    @GetMapping("/coverage/{collaborateurId}")
    public ResponseEntity<List<CompetenceCoverageDTO>> getCompetenceCoverage(@PathVariable Long collaborateurId) {
        List<CompetenceCoverageDTO> competenceCoverage = competenceService.calculateCompetenceCoverage(collaborateurId);
        return new ResponseEntity<>(competenceCoverage, HttpStatus.OK);
    }

    @GetMapping("/gap/{collaborateurId}")
    public ResponseEntity<List<CompetenceGapDTO>> getCompetenceGap(@PathVariable Long collaborateurId) {
        List<CompetenceGapDTO> competenceGaps = competenceService.getCompetenceGap(collaborateurId);
        return ResponseEntity.ok(competenceGaps);
    }



    @GetMapping("/{departmentId}/competence-coverage")
    public ResponseEntity<List<CompetenceCoverageDTO>> getCompetenceCoverageByDepartment(@PathVariable Long departmentId) {
        List<CompetenceCoverageDTO> competenceCoverage = competenceService.calculateCompetenceCoverageByDepartment(departmentId);
        return ResponseEntity.ok(competenceCoverage);
    }


    @GetMapping("/improvement-rate/departments")
    public ResponseEntity<List<CompetenceImprovementRateDTO>> getCompetenceImprovementRateForAllDepartments() {
        List<CompetenceImprovementRateDTO> improvementRates = competenceService.calculateCompetenceImprovementRateForAllDepartments();
        return ResponseEntity.ok(improvementRates);
    }


    @GetMapping("/enrollment-count/departments")
        public ResponseEntity<List<EnrollmentCountDTO>> getEnrollmentCountByDepartment() {
            List<EnrollmentCountDTO> enrollmentCounts = competenceService.calculateEnrollmentCountForAllDepartments();
            return ResponseEntity.ok(enrollmentCounts);
    }


    @GetMapping("/departmentCompetenceGap")
    public ResponseEntity<List<DepartmentCompetenceGapDTO>> getCompetenceGapForAllDepartments() {
        List<DepartmentCompetenceGapDTO> competenceGaps = competenceService.calculateCompetenceGapForAllDepartments();
        return ResponseEntity.ok(competenceGaps);
    }


    @GetMapping("/current-competency-gap")
    public ResponseEntity<CompetenceComparisonResult> getCurrentCompetencyGaps() {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collaborateur collaborator = (Collaborateur) authentication.getPrincipal();

        // Compare collaborator to their current position
        CompetenceComparisonResult result = competenceComparisonService.compareCollaboratorWithCurrentPoste(collaborator.getId());

        return ResponseEntity.ok(result);
    }



    /// RESPONSIBLE KPIS



    @GetMapping("/team/{teamId}/competence-gaps")
    public ResponseEntity<List<CompetenceGapDTO>> getCompetenceGapsForTeam(@PathVariable Long teamId) {
        List<CompetenceGapDTO> teamGaps = competenceService.getCompetenceGapsForTeam(teamId);
        return ResponseEntity.ok(teamGaps);
    }


    @GetMapping("/team/{teamId}/competence-coverage")
    public ResponseEntity<List<CompetenceCoverageDTO>> getCompetenceCoverageByTeam(@PathVariable Long teamId) {
        List<CompetenceCoverageDTO> teamCoverage = competenceService.calculateCompetenceCoverageByTeam(teamId);
        return ResponseEntity.ok(teamCoverage);
    }
}
