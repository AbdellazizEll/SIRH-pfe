package com.example.anywrpfe.controller.kpis;

import com.example.anywrpfe.dto.CollaboratorAbsenceDTO;
import com.example.anywrpfe.dto.CollaboratorCompetenceGrowthDTO;
import com.example.anywrpfe.dto.CollaboratorTrainingStatsDTO;
import com.example.anywrpfe.services.CollaborateurService;
import com.example.anywrpfe.services.CompetenceHistoryService;
import com.example.anywrpfe.services.DemandeAbsenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("collaborateur-kpi")
@Slf4j
@RequiredArgsConstructor
public class CollaboratorKPIController {

    private final CollaborateurService collaborateurService;
    private final DemandeAbsenceService demandeAbsenceService;

    private final CompetenceHistoryService competenceHistoryService;
    @GetMapping("/most-absences")
    public ResponseEntity<List<CollaboratorAbsenceDTO>> getCollaboratorWithMostAbsences() {
        try {
            List<CollaboratorAbsenceDTO> result = demandeAbsenceService.findCollaboratorWithMostAbsences();
            if (result.isEmpty()) {
                return ResponseEntity.noContent().build(); // If no data, return 204 No Content
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error retrieving collaborator with most absences: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(new CollaboratorAbsenceDTO(null, null, null, null, 0L)));
        }
    }
    @GetMapping("/least-absences")
    public ResponseEntity<?> getCollaboratorWithLeastAbsences() {
        try {
            List<CollaboratorAbsenceDTO> result = demandeAbsenceService.findCollaboratorWithLeastAbsences();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();  // Log the error details
            return new ResponseEntity<>("Error retrieving collaborator with most absences: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/highest-training-completion")
    public ResponseEntity<?> getCollaboratorWithHighestTrainingCompletion() {
        try {
            CollaboratorTrainingStatsDTO collaboratorDTO = collaborateurService.getCollaboratorWithHighestTrainingCompletion();
            if (collaboratorDTO == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No collaborator found with training completions."); // Return 204 if no collaborator is found
            }
            return ResponseEntity.ok(collaboratorDTO);
        } catch (Exception e) {
            log.error("Error retrieving collaborator with highest training completions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Unable to retrieve collaborator with highest training completions.");
        }
    }
    @GetMapping("/highest-competence-growth")
    public ResponseEntity<?> getCollaboratorWithHighestCompetenceGrowth() {
        try {
            Pageable pageable = PageRequest.of(0, 10); // Example: Get top 10 collaborators
            List<CollaboratorCompetenceGrowthDTO> result = competenceHistoryService.findCollaboratorWithHighestCompetenceGrowth(pageable);
            if (result == null || result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No competence growth found for collaborators.");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error retrieving collaborator with highest competence growth: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving collaborator with highest competence growth.");
        }
    }
}
