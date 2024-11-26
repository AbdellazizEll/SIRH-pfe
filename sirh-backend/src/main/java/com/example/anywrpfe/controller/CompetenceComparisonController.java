package com.example.anywrpfe.controller;

import com.example.anywrpfe.dto.FormationDetailDTO;
import com.example.anywrpfe.entities.CompetenceComparisonResult;
import com.example.anywrpfe.services.PosteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/comparison")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CompetenceComparisonController {

    private static final Logger log = LoggerFactory.getLogger(CompetenceComparisonController.class);
    private final PosteService competenceComparisonService;

    private static final String ERROR_MESSAGE = "An error occurred while processing the request. Please try again later.";

    @GetMapping("/{collaborateurId}/{posteId}")
    public ResponseEntity<?> compareCollaborateurWithPoste(@PathVariable Long collaborateurId, @PathVariable Long posteId) {
        try {
            log.info("Comparing collaborator with ID {} to poste with ID {}", collaborateurId, posteId);
            CompetenceComparisonResult result = competenceComparisonService.compareCollaboratorToPoste(collaborateurId, posteId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error comparing collaborator with ID {} to poste with ID {}", collaborateurId, posteId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/suggested-trainings/{collaborateurId}/{posteId}")
    public ResponseEntity<?> getFilteredSuggestedTrainings(
            @PathVariable Long collaborateurId,
            @PathVariable Long posteId,
            @RequestParam(required = false) Long competenceId,
            @RequestParam(required = false) Integer currentLevel,
            @RequestParam(required = false) Integer targetLevel) {

        try {
            log.info("Fetching suggested trainings for collaborator with ID {} and poste with ID {}, with optional filters - Competence ID: {}, Current Level: {}, Target Level: {}",
                    collaborateurId, posteId, competenceId, currentLevel, targetLevel);

            List<FormationDetailDTO> filteredTrainings = competenceComparisonService.getFilteredSuggestedTrainings(
                    collaborateurId, posteId, competenceId, currentLevel, targetLevel);
            return ResponseEntity.ok(filteredTrainings);
        } catch (Exception e) {
            log.error("Error fetching suggested trainings for collaborator with ID {} and poste with ID {}", collaborateurId, posteId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/suggested-trainings/team/{managerId}")
    public ResponseEntity<?> getTeamSuggestedTrainings(
            @PathVariable Long managerId,
            @RequestParam(required = false) Long competenceId,
            @RequestParam(required = false) Integer currentLevel,
            @RequestParam(required = false) Integer targetLevel) {

        try {
            log.info("Fetching team suggested trainings for manager with ID {}, with optional filters - Competence ID: {}, Current Level: {}, Target Level: {}",
                    managerId, competenceId, currentLevel, targetLevel);

            List<FormationDetailDTO> teamSuggestedTrainings = competenceComparisonService.getTeamSuggestedTrainings(
                    managerId, competenceId, currentLevel, targetLevel);
            return ResponseEntity.ok(teamSuggestedTrainings);
        } catch (Exception e) {
            log.error("Error fetching team suggested trainings for manager with ID {}", managerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_MESSAGE);
        }
    }
}
