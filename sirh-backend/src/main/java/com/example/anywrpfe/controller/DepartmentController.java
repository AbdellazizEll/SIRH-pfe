package com.example.anywrpfe.controller;

import com.example.anywrpfe.dto.DepartementDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Departement;
import com.example.anywrpfe.repositories.DepartementRepository;
import com.example.anywrpfe.services.DepartementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static java.time.LocalTime.now;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {
    private final DepartementRepository departementRepository;

    private final DepartementService departementService;

    @PostMapping
    public ResponseEntity<Departement> createDepartement(@RequestBody DepartementDTO departement) {
        Departement createdDepartement = departementService.createDepartement(departement);
        return new ResponseEntity<>(createdDepartement, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartementDTO> getDepartementById(@PathVariable Long id) {

        Optional<DepartementDTO> departementDTO = departementService.getDepartementById(id);
        return  departementDTO.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartementDTO> updateDepartmentDetails(
            @PathVariable Long id,
            @RequestBody DepartementDTO departementDTO) {
        Departement updatedDepartment = departementService.updateDepartmentDetails(id, departementDTO);
        return ResponseEntity.ok(DepartementDTO.fromEntity(updatedDepartment));
    }

    @PutMapping("/assign-manager/{departmentId}/{collaboratorId}")
    public ResponseEntity<Void> setManagerForDepartment(@PathVariable Long departmentId, @PathVariable Long collaboratorId) {
        departementService.setManagerForDepartment(departmentId,collaboratorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{departmentId}/responsible/{collaboratorId}")
    public ResponseEntity<Void> unassignCollaborator(@PathVariable Long departmentId, @PathVariable Long collaboratorId) {
        departementService.unassignResponsible(departmentId, collaboratorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<DepartementDTO>> getAllDepartments(@RequestParam Optional<String> name,
                                                               @RequestParam(required = false, defaultValue = "0") int page,
                                                             @RequestParam(required = false, defaultValue = "10") int size
    ) {

        String departmentName = name.orElse("");
        log.info("Fetching departments for page {} of size {}", page, size);
        Page<DepartementDTO> departmentPage = departementService.getAllDepartementsPage(departmentName, page, size);
        return ResponseEntity.ok(departmentPage);
    }


    @GetMapping("/total-collaborators/{departmentId}")
    public ResponseEntity<Integer> getTotalCollaboratorsInDepartment(@PathVariable Long departmentId) {
        try {
            int totalCollaborators = departementService.calculateTotalCollaboratorsInDepartment(departmentId);
            return ResponseEntity.ok(totalCollaborators);
        } catch (Exception e) {
            log.error("Error calculating total collaborators in department: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }


}
