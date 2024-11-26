package com.example.anywrpfe.controller;

import com.example.anywrpfe.auth.response.HttpResponse;
import com.example.anywrpfe.dto.FormationDTO;
import com.example.anywrpfe.entities.Formation;
import com.example.anywrpfe.services.CatalogueService;
import com.example.anywrpfe.services.DemandeFormationService;
import com.example.anywrpfe.services.FormationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalTime.now;

@RestController
@RequestMapping("/formation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class FormationController {
    ///FORMATION
    private final FormationService formationService;

    private final CatalogueService catalogueService;

    private final DemandeFormationService demandeFormationService;

    // FormationController.java

    @GetMapping("/getAllFormations")
    public ResponseEntity<HttpResponse> getAllFormations(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long competenceId,
            @RequestParam(required = false) Long catalogueId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<FormationDTO> formationsPage = formationService.getAllFormations(query, competenceId, catalogueId, page, size);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", formationsPage))
                        .message("Page des formations")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping
    public ResponseEntity<HttpResponse> addFormation(@RequestBody FormationDTO formation) {
        log.info("Received FormationDTO: " + formation);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("", formationService.addFormation(formation)))
                        .message("Formation a été crée")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HttpResponse> update(@PathVariable Long id, @RequestBody FormationDTO formation) {
        log.info("Received updated FormationDTO: " + formation);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("", formationService.modifierFormation(id,formation)))
                        .message("Formation a été crée")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<HttpResponse> getFormationById(
            @PathVariable Long id) {


            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(Map.of("formation", formationService.getFormationById(id)))
                            .message("No formations found for the given Catalogue ID")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }

    @GetMapping("/{idCatalogue}")
    public ResponseEntity<HttpResponse> getFormationsByCatalogue(
            @PathVariable Long idCatalogue,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FormationDTO> formations = formationService.getFormationsByCatalogue(idCatalogue, PageRequest.of(page,size));
        if (formations.isEmpty()) {
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .data(Map.of("page", formations))
                            .message("No formations found for the given Catalogue ID")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );

        }
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", formations))
                        .message("Formations par Catalogue")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());

    }


    @GetMapping("/formations/{idCompetence}")
    public ResponseEntity<HttpResponse> getFormationsByTargetCompetence(
            @PathVariable Long idCompetence,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FormationDTO> formations = formationService.getFormationByTargetCompetenceId(idCompetence, page,size).map(FormationDTO::fromEntity);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page", formations))
                        .message("Formations par competence")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());

    }

    @DeleteMapping("/{id}")
    public void deleteFormation(@PathVariable Long id) {
        formationService.deleteFormation(id);
    }

    /// END FORMATION

}
