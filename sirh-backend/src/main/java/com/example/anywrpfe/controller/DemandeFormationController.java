package com.example.anywrpfe.controller;

import com.example.anywrpfe.auth.response.HttpResponse;
import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.DemandeFormation;
import com.example.anywrpfe.entities.Enum.StatusType;
import com.example.anywrpfe.services.DemandeFormationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Map;

import static java.time.LocalTime.now;
import static javax.security.auth.callback.ConfirmationCallback.OK;

@RestController
@RequestMapping("/formations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class DemandeFormationController {


    private final DemandeFormationService demandeFormationService;


    private static final String ACCEPTED = "ACCEPTED";
    private static final String PENDING = "PENDING";
    private static final String REJECTED = "REJECTED";



    /// Demande Formations
    @GetMapping("/getAllRequests")
    public ResponseEntity<HttpResponse> getAllRequests(
            @RequestParam(required = false) StatusType status, // Accept status as an Enum
            @RequestParam(required = false) Long competenceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DemandeFormationDTO> demandes = demandeFormationService.getAllRequests(status, competenceId, page, size);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", demandes))
                        .message("Demande de formations retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build()
        );
    }

    @GetMapping("/Demande/managerHistory/{managerId}")
    public ResponseEntity<HttpResponse> getManagerHistory(
            @PathVariable Long managerId,
            @RequestParam(required = false) StatusType status, // Accept status as an Enum
            @RequestParam(required = false) Long competenceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {


        Page<DemandeFormationDTO> demandes = demandeFormationService.getManagerTrainingRequests(managerId,status, competenceId, page, size);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", demandes))
                        .message("Demande de formations retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build()
        );
    }




    @PostMapping("/Demande")
    public ResponseEntity<HttpResponse> addRequest(@RequestBody DemandeFormationDTO request) {

        DemandeFormation demandeFormation = demandeFormationService.addRequest(request);

        // Convert the saved entity to a DTO before returning it
        DemandeFormationDTO responseDTO = DemandeFormationDTO.fromEntity(demandeFormation);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("data",responseDTO))
                        .message("Demande Formation a été crée ")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());
    }

    @PostMapping("/Demande/createCustomized")
    public ResponseEntity<HttpResponse> addCustomizedRequest(@RequestBody CustomizedRequestFormationDTO requestDTO) {
        try {
            DemandeFormation demandeFormation = demandeFormationService.addCustomizedRequest(requestDTO.getRequest(), requestDTO.getFormation());

            DemandeFormationDTO responseDTO = DemandeFormationDTO.fromEntity(demandeFormation);

            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timestamp(new Date().toString())
                            .data(Map.of("data", responseDTO))
                            .message("Customized request created successfully")
                            .status(HttpStatus.OK)
                            .build());
        } catch (Exception e) {
            log.error("Error in creating customized request: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    HttpResponse.builder()
                            .timestamp(new Date().toString())
                            .message("Error occurred: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        }
    }

    @PutMapping("/Demande/approve/{id}")
    public ResponseEntity<Void> approveRequest(@PathVariable Long id) {

        demandeFormationService.approveRequest(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/Demande/reject/{id}")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long id,  @RequestBody String rejectionReason) {

        demandeFormationService.rejectRequest(id,rejectionReason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assignCollaborator")
    public ResponseEntity<HttpResponse> assignCollaboratorToTraining(
            @RequestBody AssignCollaboratorRequest request
    ) {
        try {
            demandeFormationService.assignCollaboratorToTraining(request.getCollaboratorId(), request.getFormationId());
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timestamp(new Date().toString())
                            .message("Collaborator assigned to the training successfully")
                            .status(HttpStatus.OK)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    HttpResponse.builder()
                            .timestamp(new Date().toString())
                            .message("Failed to assign collaborator to the training")
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
    }
    /// END Demande Formations




    @GetMapping("/findById/{id}")
    public ResponseEntity<HttpResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("",demandeFormationService.getById(id)))
                        .message("Training request retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());
    }


    // Endpoint to create a request from a suggested training
    @PostMapping("/create-from-suggested")
    public ResponseEntity<DemandeFormationDTO> createFromSuggestedTraining(
            @RequestBody SuggestedTrainingRequestDTO requestDTO) {
        try {
            // Call the service method to create a request from suggested training
            DemandeFormation demandeFormation = demandeFormationService.createRequestFromSuggestedTraining(
                    requestDTO.getFormationId(),
                    requestDTO.getManagerId(),
                    requestDTO.getCollaboratorId()
            );

            // Convert to DTO for response
            DemandeFormationDTO responseDTO = DemandeFormationDTO.fromEntity(demandeFormation);

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
