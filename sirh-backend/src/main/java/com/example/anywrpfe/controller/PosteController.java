package com.example.anywrpfe.controller;

import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.entities.Poste;
import com.example.anywrpfe.entities.PosteCompetence;
import com.example.anywrpfe.services.CollaborateurService;
import com.example.anywrpfe.services.PosteCompetenceService;
import com.example.anywrpfe.services.PosteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/poste")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class PosteController {
    private final PosteService posteService;
    private final PosteCompetenceService posteCompetenceService;

    private final CollaborateurService collaborateurService;

    @PostMapping("/create")
    public ResponseEntity<Poste> createPoste(@RequestBody PosteDTO posteDTO) {
        Poste poste = posteService.createPoste(posteDTO);
        return new ResponseEntity<>(poste, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PosteDTO> updatePoste(@PathVariable Long id, @RequestBody PosteDTO posteDTO) {
        PosteDTO poste = posteService.updatePoste(id, posteDTO);
        return new ResponseEntity<>(poste, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PosteDTO> getById(@PathVariable Long id) {
        Optional<PosteDTO> poste = posteService.getPosteById(id);
        return poste.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoste(@PathVariable Long id) {
        posteService.removePoste(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<List<PosteDTO>> findAllPosts()
    {
        List<PosteDTO> postes = posteService.findAll();



        return new ResponseEntity<>(postes, HttpStatus.OK);
    }

    // POSTE-COMPETENCE

    @PostMapping("/Poste-Competence/{posteId}/{competenceId}")
    public ResponseEntity<PosteDTO> createPosteCompetence(
            @PathVariable Long posteId,
            @PathVariable Long competenceId,
            @RequestParam String evaluation) {
        Poste posteCompetence = posteCompetenceService.addCompetenceToPoste(posteId, competenceId, evaluation);
        PosteDTO dto = PosteDTO.fromEntity(posteCompetence);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }



    @GetMapping("/Poste-competence/competence/{id}")
    public ResponseEntity<List<PosteCompetenceDTO>> getPosteByCompetence(@PathVariable Long id) {
        List<PosteCompetence> posteCompetenceList = posteCompetenceService.getPosteByCompetence(id);
        List<PosteCompetenceDTO> dtoList = posteCompetenceList.stream()
                .map(PosteCompetenceDTO::fromEntity)
                .toList();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/Poste-competence/getByType/{id}")
    public ResponseEntity<List<PosteCompetenceDTO>> getCompetenceByType(
            @PathVariable Long id,
            @RequestParam TypeEval scaleType) {
        List<PosteCompetence> posteCompetenceList = posteCompetenceService.getCompetencesByType(id, scaleType);
        List<PosteCompetenceDTO> dtoList = posteCompetenceList.stream()
                .map(PosteCompetenceDTO::fromEntity)
                .toList();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/current/{posteId}")
    public ResponseEntity<Set<PosteCompetenceDTO>> getCurrentCompetence(@PathVariable Long posteId
    ) {
        List<PosteCompetence> competences = posteCompetenceService.getPosteCompetence(posteId);
        Set<PosteCompetenceDTO> competenceDTOs = competences.stream()
                .map(PosteCompetenceDTO::fromEntity)
                .collect(Collectors.toSet());

        // Return the DTOs in the response entity
        return ResponseEntity.ok(competenceDTOs);
    }

    @PutMapping("/postes/{posteId}/collaborators/{collaboratorId}")
    public ResponseEntity<Void> unassignCollaborator(@PathVariable Long posteId, @PathVariable Long collaboratorId) {
        posteService.unassignCollaboratorFromPoste(posteId, collaboratorId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/Poste-competence/{id}")
    public ResponseEntity<PosteCompetenceDTO> findById(@PathVariable Long id) {
        PosteCompetence posteCompetence = posteCompetenceService.getById(id);
        PosteCompetenceDTO dto = PosteCompetenceDTO.fromEntity(posteCompetence);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/Poste-competence/")
    public ResponseEntity<List<PosteCompetenceDTO>> getAll() {
        List<PosteCompetence> posteCompetenceList = posteCompetenceService.getAllPosts();
        List<PosteCompetenceDTO> dtoList = posteCompetenceList.stream()
                .map(PosteCompetenceDTO::fromEntity)
                .toList();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PutMapping("/{collaborateurId}/assignPoste/{posteId}")
    public ResponseEntity<CollaboratorDTO> assignPosteToCollaborateur(@PathVariable Long collaborateurId, @PathVariable Long posteId) {

        CollaboratorDTO collaboratorDTO = collaborateurService.assignPosteToCollaborateur(collaborateurId,posteId);
        return new ResponseEntity<>(collaboratorDTO, HttpStatus.OK);
    }

    @PutMapping("/{collaborateurId}/removePoste")
    public ResponseEntity<CollaboratorDTO> removePosteFromCollaborateur(@PathVariable Long collaborateurId) {
        CollaboratorDTO collaborateur = collaborateurService.removePosteFromCollaborateur(collaborateurId);
        return new ResponseEntity<>(collaborateur, HttpStatus.OK);
    }
}
