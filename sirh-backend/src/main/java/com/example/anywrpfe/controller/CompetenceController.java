package com.example.anywrpfe.controller;

import com.example.anywrpfe.auth.response.HttpResponse;
import com.example.anywrpfe.dto.CollaborateurCompetenceDTO;
import com.example.anywrpfe.dto.CollaboratorDTO;
import com.example.anywrpfe.dto.CompetenceDTO;
import com.example.anywrpfe.dto.PosteCompetenceDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.CollaborateurCompetence;
import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.services.CollaborateurCompetenceService;
import com.example.anywrpfe.services.CompetenceService;
import com.example.anywrpfe.services.PosteCompetenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;
import static javax.security.auth.callback.ConfirmationCallback.OK;


@RestController
@RequestMapping("/competences")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CompetenceController {

    private final CompetenceService competenceService;
    private final CollaborateurCompetenceService collaborateurCompetenceService;

    private final PosteCompetenceService posteCompetenceService;


    @PostMapping("/create")
    public ResponseEntity<Competence> createCompetence(@RequestBody CompetenceDTO competence)
    {
        Competence createdcompetence = competenceService.createCompetence(competence);
        return new ResponseEntity<>(createdcompetence, HttpStatus.CREATED);

    }


    @GetMapping("/scaleTypes")
    public ResponseEntity<List<String>> getScaleTypes() {
        List<String> scaleTypes = Arrays.stream(TypeEval.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(scaleTypes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Competence> updateCompetence(@PathVariable Long id , @RequestBody Competence competence)
    {
        Competence updatedCompetence = competenceService.updateCompetence(id, competence);
        return ResponseEntity.ok(updatedCompetence);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetence(@PathVariable Long id )
    {
        competenceService.removeCompetence(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllCompetence")
    public ResponseEntity<HttpResponse> getAllCompetences(@RequestParam Optional<String> name,
                                                              @RequestParam Optional<Integer> page,
                                                              @RequestParam Optional<Integer> size) {
        Page<CompetenceDTO> competences = competenceService.getAllCompetences(name.orElse(""),page.orElse(0) ,size.orElse(10));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page",competences))
                        .message("Collaborator retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Competence> getCompetenceById(@PathVariable Long id) {
        Competence competence = competenceService.getCompetenceById(id)
                .orElseThrow(() -> new RuntimeException("Competence not found"));
        return ResponseEntity.ok(competence);
    }


    ///COLLABORATEUR-COMPETENCE SERVICE ! /////

    @PostMapping("/{collaborateurId}/{competenceId}")
    public ResponseEntity<CollaboratorDTO> addCompetenceToCollaborateur(@PathVariable Long collaborateurId,
                                                                        @PathVariable Long competenceId,
                                                                        @RequestParam String evaluation) {
        Collaborateur collaborateur = collaborateurCompetenceService.addCompetenceToCollaborateur(collaborateurId, competenceId, evaluation);
        CollaboratorDTO collaborateurDTO = CollaboratorDTO.fromEntity(collaborateur);
        return ResponseEntity.ok(collaborateurDTO);
    }
    @PutMapping("updateCompetenceCollaborateur/{collaborateurId}/{competenceId}")
    public ResponseEntity<CollaborateurCompetenceDTO> updateCompetenceEvaluation(@PathVariable Long collaborateurId,
                                                                    @PathVariable Long competenceId,
                                                                    @RequestParam String evaluation,
                                                                    TypeEval newScaleType
                                                                   ) {
        CollaborateurCompetenceDTO collaborateurCompetence = collaborateurCompetenceService.updateCompetenceEvaluation(collaborateurId,competenceId,evaluation,newScaleType);
        return ResponseEntity.ok(collaborateurCompetence);
    }
    @PutMapping("updateCompetencePoste/{posteId}/{competenceId}")
    public ResponseEntity<PosteCompetenceDTO> updateCompetencePoste(
            @PathVariable Long posteId,
            @PathVariable Long competenceId,
            @RequestParam String evaluation,
            @RequestParam(required = false) TypeEval newScaleType // Ensure this is passed or optional
    ) {
        PosteCompetenceDTO posteCompetenceDTO = posteCompetenceService.updateCompetencePosteEvaluation(
                posteId, competenceId, evaluation, newScaleType
        );
        return ResponseEntity.ok(posteCompetenceDTO);
    }
    @DeleteMapping("/remove/{collaborateurId}/{competenceId}")
    public ResponseEntity<Void> removeCompetenceFromCollaborateur(@PathVariable Long collaborateurId,
                                                                  @PathVariable Long competenceId) {
        collaborateurCompetenceService.removeCompetenceFromCollaborateur(collaborateurId, competenceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/collab/{collaborateurId}")
    public ResponseEntity<List<CollaborateurCompetence>> getCompetencesForCollaborateur(@PathVariable Long collaborateurId) {
        List<CollaborateurCompetence> competences = collaborateurCompetenceService.getCompetencesForCollaborateur(collaborateurId);
        return ResponseEntity.ok(competences);
    }


    @GetMapping("/{collaborateurId}/type")
    public ResponseEntity<List<CollaborateurCompetence>> getCompetencesByType(@PathVariable Long collaborateurId,
                                                                              @RequestParam TypeEval scaleType) {
        List<CollaborateurCompetence> competences = collaborateurCompetenceService.getCompetencesByType(collaborateurId, scaleType);
        return ResponseEntity.ok(competences);
    }

    @GetMapping("/available/{collaborateurId}")
    public ResponseEntity<List<CompetenceDTO>> getAvailableCompetence(@PathVariable Long collaborateurId
                                                                              ) {
        List<Competence> competences = collaborateurCompetenceService.fetchAvailableCompetence(collaborateurId);

        //
        List<CompetenceDTO> competenceDTOS = competences.stream()
                .map(CompetenceDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(competenceDTOS);

    }

    @GetMapping("/current/{collaborateurId}")
    public ResponseEntity<Set<CollaborateurCompetenceDTO>> getCurrentCompetence(@PathVariable Long collaborateurId
    ) {
        Set<CollaborateurCompetence> competences = collaborateurCompetenceService.fetchCurrentCompetence(collaborateurId);
        Set<CollaborateurCompetenceDTO> competenceDTOs = competences.stream()
                .map(CollaborateurCompetenceDTO::fromEntity)
                .collect(Collectors.toSet());

        // Return the DTOs in the response entity
        return ResponseEntity.ok(competenceDTOs);
    }




    @GetMapping("/possibleValues/{competenceId}")
    public ResponseEntity<List<String>> fetchPossibleValuesOfCompetence(@PathVariable Long competenceId)
    {
        List<String> possibleValues = competenceService.fetchPossibleValuesOfCompetence(competenceId);

        return ResponseEntity.ok(possibleValues);
    }


}
