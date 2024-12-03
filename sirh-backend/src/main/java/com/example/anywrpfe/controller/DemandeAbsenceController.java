package com.example.anywrpfe.controller;


import com.example.anywrpfe.auth.response.HttpResponse;
import com.example.anywrpfe.dto.AbsenceDTO;
import com.example.anywrpfe.dto.DemandeAbsenceDTO;
import com.example.anywrpfe.dto.LightDemandeAbsenceDTO;
import com.example.anywrpfe.entities.Absence;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.repositories.CollaborateurRepository;

import com.example.anywrpfe.requests.DemandeAbsenceRequest;
import com.example.anywrpfe.services.AbsenceService;
import com.example.anywrpfe.services.DemandeAbsenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.now;
import static javax.security.auth.callback.ConfirmationCallback.OK;

@RestController
@RequestMapping("/demandeAbsence")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class DemandeAbsenceController {
    private final CollaborateurRepository collaborateurRepository;

    private final AbsenceService absenceService;

    private final DemandeAbsenceService demandeAbsenceService;

    private static final String ABSENCE_REQUEST="ABSENCE REQUESTS RETRIEVED";


    private static final String ACCEPTED = "ACCEPTED";
    private static final String PENDING = "PENDING";
    private static final String REJECTED = "REJECTED";
    @PostMapping("/createAbsence")
    public ResponseEntity<HttpResponse> createAbsence(@RequestBody Absence absence) {
        Absence createdAbsence = absenceService.saveAbsence(absence);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("absence", createdAbsence))
                        .message("Absence has been created")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    ///common acitivities
    @GetMapping("/getAllMotifs")
    public ResponseEntity<List<AbsenceDTO>> getMotifs()
    {

        log.info("Fetching absence Motifs");
        List<AbsenceDTO> motifs = demandeAbsenceService.getAllMotifs();
        return ResponseEntity.ok(motifs);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<HttpResponse> createDemandeAbsence(
            @ModelAttribute DemandeAbsenceRequest request,
            @RequestParam(value = "file", required = false) MultipartFile justificatif) throws IOException {

        // Log incoming request data for debugging
        log.info("Received DemandeAbsenceRequest: dateDebut={}, dateFin={}, comment={}, motif={}",
                request.getDateDebut(), request.getDateFin(), request.getComment(), request.getMotif());

        // Log if a file has been received
        log.info("Received justificatif file: {}", justificatif != null ? justificatif.getOriginalFilename() : "No file provided");

        try {
            // Call the service to process the request and save the absence
            DemandeAbsenceDTO savedAbsence = demandeAbsenceService.AddDemandeAbs(request, justificatif);
            log.info("DemandeAbsence created successfully: {}", savedAbsence);

            // Return success response
            return ResponseEntity.ok(HttpResponse.builder()
                    .timestamp(now().toString())
                    .data(Map.of("data", savedAbsence))
                    .message("Demande d'absence a été créée avec succès")
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build());
        } catch (Exception e) {
            // Log error details
            log.error("Error creating DemandeAbsence: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("Une erreur s'est produite. Veuillez réessayer.")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }


    @PostMapping("/testUpload")
    public ResponseEntity<String> testUpload(@RequestParam("file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            return ResponseEntity.ok("File received: " + file.getOriginalFilename());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File not received");
        }
    }


    @GetMapping("/my-absence-requests")
    public ResponseEntity<HttpResponse> getMyAbsenceRequests(@RequestParam(required = false, defaultValue = "0") int page,
                                                             @RequestParam(required = false, defaultValue = "10") int size,
                                                             @RequestParam(required = false) String status

    ) {
        Page<LightDemandeAbsenceDTO> absencePage;

        if (status == null || status.isEmpty()) {
            // Fetch all requests for the manager
            absencePage = demandeAbsenceService.MesDemandeAbsence(status, page, size);
        } else if (status.equalsIgnoreCase(PENDING)) {
            absencePage = demandeAbsenceService.MesDemandeAbsence(status, page, size);
        } else if (status.equalsIgnoreCase(ACCEPTED)) {
            absencePage = demandeAbsenceService.MesDemandeAbsence(status, page, size);
        } else if (status.equalsIgnoreCase(REJECTED)) {
            absencePage = demandeAbsenceService.MesDemandeAbsence(status,page, size);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status parameter");
        }

        log.info("Fetching absence requests with status '{}', page {} of size {}", status, page, size);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", absencePage))
                        .message(String.format("%s Absence Requests retrieved for logged user", status != null ? status : "All"))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }






    @PutMapping("/absence-requests/{demandeId}/reject")
    public ResponseEntity<DemandeAbsenceDTO> rejectAbsenceRequest(@PathVariable Long demandeId , @RequestBody Map<String,String> requestBody){

        String refusalReason = requestBody.get("refusalReason");
        DemandeAbsenceDTO rejectedDemande = demandeAbsenceService.rejectDemande(demandeId, refusalReason);
        return ResponseEntity.ok().body(rejectedDemande);
    }



    /// end of common activities

    ///RH ACTIVITIES
    @GetMapping("/absence-requests")
    public ResponseEntity<HttpResponse> getAllAbsenceRequests(@RequestParam(required = false, defaultValue = "0") int page,
                                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Fetching absence requests, page {} of size {}", page, size);




        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("", demandeAbsenceService.ListDemandeAbsence(page, size)))
                        .message(ABSENCE_REQUEST)
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());


    }




    @GetMapping("/absence-requests-department")
    public ResponseEntity<HttpResponse> getAllAbsenceRequestsByDepartmentName(
            @RequestParam(required = false ,defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Fetching absence requests of Department, page {} of size {}", page, size);


        Page<LightDemandeAbsenceDTO> allAbsences = demandeAbsenceService.fetchAbsenceByDepartmentName(name,page,size);


        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("", allAbsences))
                        .message(ABSENCE_REQUEST)
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());


    }


    @PutMapping("/absence-requests-rh/{demandeId}/approve")
    public ResponseEntity<DemandeAbsenceDTO> approveAbsenceRequestRh(@PathVariable Long demandeId){
        DemandeAbsenceDTO approveDemande = demandeAbsenceService.approveByRH(demandeId);
        return ResponseEntity.ok    ().body(approveDemande);
    }

    @GetMapping("/absence-requests-rh")
    public ResponseEntity<HttpResponse> getAbsencesRH(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String absenceType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        var absencePage = demandeAbsenceService.getAbsencesRH(
                status, absenceType, startDate, endDate, page, size
        );

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", absencePage))
                        .message(ABSENCE_REQUEST)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }


    // END RH ACTIVITIES



    @PutMapping("/absence-requests/{demandeId}/approve")
    public ResponseEntity<DemandeAbsenceDTO> approveAbsenceRequest(@PathVariable Long demandeId){
        DemandeAbsenceDTO approveDemande = demandeAbsenceService.approveDemandeManagerEquipe(demandeId);
        return ResponseEntity.ok().body(approveDemande);
    }


    @GetMapping("/manager/absence-requests/{managerId}")
    public ResponseEntity<HttpResponse> getAbsencesManager(
            @PathVariable Long managerId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String absenceType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        var absencePage = demandeAbsenceService.getAbsencesManager(
                managerId, status, absenceType, startDate, endDate, page, size
        );

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", absencePage))
                        .message(ABSENCE_REQUEST)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    // End Manager Activities
    //Responsible Activities

    @GetMapping("/department/absence-requests")
    public ResponseEntity<HttpResponse> getAuthenticatedUserDepartmentAbsenceRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<LightDemandeAbsenceDTO> absenceRequests = demandeAbsenceService.getAbsenceByAuthenticatedResponsibleDepartment(page, size);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("page", absenceRequests))
                        .message(ABSENCE_REQUEST)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/absence-requests-department/{departmentId}")
    public ResponseEntity<HttpResponse> getAllAbsenceRequestsDepartment(@PathVariable Long departmentId , @RequestParam(required = false, defaultValue = "0") int page,
                                                                        @RequestParam(required = false, defaultValue = "10") int size
    ) {

        Page<LightDemandeAbsenceDTO> absencePage = demandeAbsenceService.getAbsenceByDepartment(departmentId, page, size);


        log.info("Fetching absence requests for Department ID {} , page {} of size {}", departmentId, page, size);


        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", absencePage))
                        .message(ABSENCE_REQUEST)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/responsible/absence-requests/{responsibleId}")
    public ResponseEntity<HttpResponse> getAbsencesResponsible(
            @PathVariable Long responsibleId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String absenceType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        var absencePage = demandeAbsenceService.getAbsencesResponsible(
                responsibleId, status, absenceType, startDate, endDate, page, size
        );

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", absencePage))
                        .message(ABSENCE_REQUEST)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }





    @PutMapping("/absence-requests-department/{demandeId}/approve")
    public ResponseEntity<DemandeAbsenceDTO> approveAbsenceRequestResponsible(@PathVariable Long demandeId){
        DemandeAbsenceDTO approveDemande = demandeAbsenceService.approveByDepartmentResponsible(demandeId);
        return ResponseEntity.ok().body(approveDemande);
    }

    @PutMapping("/absence-requests-department/{demandeId}/reject")
    public ResponseEntity<DemandeAbsenceDTO> departmentRejection(@PathVariable Long demandeId , @RequestBody Map<String,String> requestBody){

        String refusalReason = requestBody.get("refusalReason");
        DemandeAbsenceDTO rejectedDemande = demandeAbsenceService.rejectDemande(demandeId, refusalReason);
        return ResponseEntity.ok().body(rejectedDemande);
    }



    @DeleteMapping("delete/{idDemande}")
    public ResponseEntity<Void> delete(@PathVariable Long idDemande)
    {

        log.info("Removing Absence Request ID {} : ", idDemande);

        demandeAbsenceService.removeDemande(idDemande);
        return ResponseEntity.ok().build();
    }

    ///End Responsible Activities




}

