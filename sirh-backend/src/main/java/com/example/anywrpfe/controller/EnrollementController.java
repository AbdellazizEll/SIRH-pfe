package com.example.anywrpfe.controller;


import com.example.anywrpfe.auth.response.HttpResponse;
import com.example.anywrpfe.dto.CompetenceHistoryDTO;
import com.example.anywrpfe.dto.EnrollmentDTO;
import com.example.anywrpfe.entities.CompetenceHistory;
import com.example.anywrpfe.services.CompetenceHistoryService;
import com.example.anywrpfe.services.EnrollementService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalTime.now;

@RequestMapping("/enrollment/")
@RestController
@RequiredArgsConstructor
@Slf4j
public class EnrollementController {

    private final EnrollementService enrollementService;
    private final CompetenceHistoryService competenceHistoryService;

    @GetMapping("collaborator/{collaboratorId}")
    public ResponseEntity<HttpResponse> getCollaboratorEnrollments(@PathVariable Long collaboratorId,
                                                                 @RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size) {
       Page<EnrollmentDTO> enrollementPage = enrollementService.getEnrollmentsByCollaboratorId(collaboratorId,page.orElse(0) ,size.orElse(10));

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", enrollementPage))
                        .message("Page des enrollements")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    @GetMapping
    public ResponseEntity<HttpResponse>  getAllEnrollements(@RequestParam Optional<Integer> page,
                                                            @RequestParam Optional<Integer> size)
    {
        Page<EnrollmentDTO> enrollementPage = enrollementService.getAllEnrollement(page.orElse(0) ,size.orElse(10));

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", enrollementPage))
                        .message("Page des enrollement")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    @PutMapping("{demandeFormationId}")
    public ResponseEntity<HttpResponse> addEnrollementToCollaborator(@PathVariable Long demandeFormationId) {
        try {
            EnrollmentDTO enrollmentDTO = enrollementService.addEnrollementToCollaborator(demandeFormationId);

            // Return a success response with the enrollment details
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(Map.of("enrollment", enrollmentDTO))
                            .message("Collaborator successfully enrolled")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        } catch (RuntimeException ex) {
            // Handle duplicate enrollment error
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message(ex.getMessage())  // Message indicating conflict (already enrolled)
                            .status(HttpStatus.CONFLICT)
                            .statusCode(HttpStatus.CONFLICT.value())
                            .build()
            );
        } catch (Exception ex) {
            // Handle general exceptions
            ex.printStackTrace();  // Log the stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("An internal server error occurred.")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build()
            );
        }
    }


    @GetMapping("{id}")
    public ResponseEntity<HttpResponse> getEnrollementById(@PathVariable Long id)
    {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("", enrollementService.getEnrollementById(id)))
                        .message("Enrollement")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PutMapping("/{collaboratorId}/{formationId}")
    public ResponseEntity<Void> updateProgressForTraining(
            @PathVariable Long collaboratorId,
            @PathVariable Long formationId,
            @RequestParam int progress) {
        enrollementService.updateProgressForTraining(collaboratorId, formationId, progress);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/collaborators/{collaboratorId}")
    public ResponseEntity<Page<EnrollmentDTO>> getEnrollmentsByCollaboratorId(
            @PathVariable Long collaboratorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<EnrollmentDTO> enrollements = enrollementService.getEnrollmentsByCollaboratorId(collaboratorId, page, size);
        return ResponseEntity.ok(enrollements);
    }

    @PutMapping("/mark-completed/{enrollmentId}")
    public ResponseEntity<String> markAsCompleted(@PathVariable Long enrollmentId) {
        enrollementService.markTrainingAsCompleted(enrollmentId);
        return ResponseEntity.ok("Training marked as completed and competencies updated");
    }

    @PutMapping("/evaluate/{enrollmentId}")
    public ResponseEntity<?> evaluateTrainingCompletion(
            @PathVariable Long enrollmentId,
            @RequestBody Map<String, Object> request) throws MessagingException {

        boolean isApproved = (Boolean) request.get("isApproved");
        String rejectionReason = (String) request.getOrDefault("rejectionReason", "No reason provided");

        enrollementService.evaluateTrainingCompletion(enrollmentId, isApproved, rejectionReason);
        return ResponseEntity.ok().body("Training evaluation completed");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeEnrollement(@PathVariable Long id) {
        enrollementService.removeEnrollement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/competence-history/{collaboratorId}")
    public ResponseEntity<List<CompetenceHistoryDTO>> getCompetenceHistory(@PathVariable Long collaboratorId) {
        List<CompetenceHistory> history = competenceHistoryService.getCompetenceHistoryForCollaborator(collaboratorId);
        List<CompetenceHistoryDTO> historyDTOs = history.stream()
                .map(CompetenceHistoryDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(historyDTOs);
    }


    ///KPIS



}
