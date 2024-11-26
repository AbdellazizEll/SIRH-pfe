package com.example.anywrpfe.controller;


import com.example.anywrpfe.auth.response.HttpResponse;
import com.example.anywrpfe.dto.EquipeDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Equipe;
import com.example.anywrpfe.repositories.EquipeRepository;
import com.example.anywrpfe.services.EquipeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalTime.now;
import static javax.security.auth.callback.ConfirmationCallback.OK;

@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class EquipeController {
    private final EquipeRepository equipeRepository;

    private final EquipeService equipeService;

    @PostMapping("createEquipe")
    public ResponseEntity<Equipe> createEquipe(@RequestBody EquipeDTO equipe) {
        Equipe createdEquipe = equipeService.createEquipe(equipe);
        return ResponseEntity.ok(createdEquipe);
    }

    @PostMapping("/departments/{depId}/add-team")
    public ResponseEntity<?> addTeamToDepartment(@PathVariable Long depId, @RequestBody EquipeDTO equipeDTO) {
        try {
            Equipe newEquipe = equipeService.createStaticEquipeDepartment(equipeDTO, depId);
            return ResponseEntity.ok(newEquipe);
        } catch (RuntimeException ex) {
            log.error("Error creating team: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/assignManager/{idManager}/{idEquipe}")
    public ResponseEntity<Equipe> assignManagerToEquipe(@PathVariable Long idManager, @PathVariable Long idEquipe) {
        try {
            Equipe updatedEquipe = equipeService.assignManagerToEquipe(idManager, idEquipe);
            return ResponseEntity.ok(updatedEquipe);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // If the manager or team is not found
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Generic error handler
        }
    }

    @PutMapping("/{idEquipe}")
    public ResponseEntity<Equipe> updateEquipe(@PathVariable Long idEquipe, @RequestBody EquipeDTO equipe) {
        Equipe updatedEquipe = equipeService.updateEquipe(idEquipe, equipe);
        return ResponseEntity.ok(updatedEquipe);
    }

    @PutMapping("/{equipeId}/collaborators/{collaboratorId}")
    public ResponseEntity<Void> unassignCollaborator(@PathVariable Long equipeId, @PathVariable Long collaboratorId) {
        equipeService.unassignCollaboratorFromEquipe(equipeId, collaboratorId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{equipeId}/manager/{collaboratorId}")
    public ResponseEntity<Void> unassignManagerFromEquipe(@PathVariable Long equipeId, @PathVariable Long collaboratorId) {
        equipeService.unassignManagerFromEquipe(equipeId, collaboratorId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/page")
    public ResponseEntity<HttpResponse> getAllEquipes(@RequestParam Optional<String> name,
                                                      @RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int size
    ) {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page",equipeService.getAllEquipes(name.orElse(""),page,size)))
                        .message("Collaborator retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());


    }



    @GetMapping("/findByEquipe/{id}")
    public ResponseEntity<EquipeDTO> getEquipeById(@PathVariable Long id)
    {
        Optional<EquipeDTO> equipe = equipeService.findEquipeById(id);
        return  equipe.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/getByManagerId/{id}")
    public ResponseEntity<EquipeDTO> getByManagerId(@PathVariable Long id)
    {
        Optional<EquipeDTO> equipe = equipeService.findEquipeByManagerId(id);
        return  equipe.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }




    @PutMapping("/assign-to-equipe")
    public ResponseEntity<Void> setCollabToEquipe(@RequestBody Collaborateur dto) {
        equipeService.setCollaboratorToEquipe(dto.getId(), dto.getEquipe().getId_equipe());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/assign-manager-toEquipe")
    public ResponseEntity<Void> setManagerToEquipe(@RequestBody Collaborateur dto) {
        equipeService.setManagerToEquipe(dto.getId(), dto.getEquipe().getId_equipe());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/assign-list-collaborators/{equipeId}")
    public  ResponseEntity<Void> setListToEquipe(@PathVariable Long equipeId, @RequestBody List<Long> collaboratorIds)
    {
        equipeService.setListofCollabs(collaboratorIds,equipeId );
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id)
    {
        equipeService.deleteTeam(id);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/remove-collab/{id}/{equipeId}")
    public ResponseEntity<Void> removeCollab(@PathVariable Long id , @PathVariable Long equipeId) {
        equipeService.removeCollabFromEquipe(id,equipeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/assignEquipeToDepartment/{idEquipe}/{idDep}")
    public ResponseEntity<Equipe> assignEquipeToDep(@PathVariable Long idEquipe, @PathVariable Long idDep) {
        try {
            Equipe equipe = equipeService.assignEquipeToDepartment(idEquipe, idDep);
            return ResponseEntity.ok(equipe);  // Return the updated team
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Return 404 if not found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Return 500 on other exceptions
        }
    }



}
