package com.example.anywrpfe.controller;


import com.example.anywrpfe.auth.ChangePasswordRequest;
import com.example.anywrpfe.auth.response.HttpResponse;
import com.example.anywrpfe.dto.CollaboratorDTO;
import com.example.anywrpfe.dto.LightCollaboratorDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Role;
import com.example.anywrpfe.services.CollaborateurService;
import com.example.anywrpfe.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;
import static javax.security.auth.callback.ConfirmationCallback.OK;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CollaboratorController {

    private final CollaborateurService collaborateurService;

    private final RoleService roleService;

    @GetMapping("/getAllCollaborators")
    public ResponseEntity<HttpResponse> getUsers(@RequestParam Optional<String> name,
                                                    @RequestParam Optional<Integer> page,
                                                    @RequestParam Optional<Integer> size)
    {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page",collaborateurService.ListCollaborators(name.orElse(""),page.orElse(0) ,size.orElse(10))))
                        .message("Collaborator retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }

    @GetMapping("/getRoles")
    public ResponseEntity<HttpResponse> getRoles()
    {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("List",roleService.findAll()))
                        .message("Collaborator retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }

    @GetMapping("/equipe-managers")
    public ResponseEntity<List<CollaboratorDTO>> getEquipeManagers() {
        List<CollaboratorDTO> managers = collaborateurService.findEquipeManagers();

        return ResponseEntity.ok(managers);
    }

    @GetMapping("/collaborators/jobless")
    public ResponseEntity<List<LightCollaboratorDTO>> joblessCollaborators() {
        List<LightCollaboratorDTO> managers = collaborateurService.findJoblessCollaborators();

        return ResponseEntity.ok(managers);
    }


    @GetMapping("/managers/jobless")
    public ResponseEntity<List<LightCollaboratorDTO>> getTeamlessManagers() {
        List<LightCollaboratorDTO> managers = collaborateurService.findEquipeManagersAvailable();

        return ResponseEntity.ok(managers);
    }

    @PutMapping("/unAssignManager/{equipeId}/{collaboratorId}")
    public ResponseEntity<Void> unAssignManager(@PathVariable Long equipeId, @PathVariable Long collaboratorId) {
        collaborateurService.unAssignManagerFromEquipe(equipeId, collaboratorId);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/collaborators/teamless")
    public ResponseEntity<List<LightCollaboratorDTO>> teamlessCollaborators() {
        List<LightCollaboratorDTO> managers = collaborateurService.findTeamlessCollaborators();

        return ResponseEntity.ok(managers);
    }


    @GetMapping("/department-responsibles")
    public ResponseEntity<List<CollaboratorDTO>> getDepartmentResponsibles() {
        List<CollaboratorDTO> responsibles = collaborateurService.findDepartmentResponsibles();
        return ResponseEntity.ok(responsibles);
    }

    @PutMapping("/unAssignResponsible/{departmentId}/{collaboratorId}")
    public ResponseEntity<Void> unAssignResponisble(@PathVariable Long departmentId, @PathVariable Long collaboratorId) {
        collaborateurService.unAssignDepartmentResponsible(departmentId, collaboratorId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/findByCollaborator/{id}")
    public ResponseEntity<CollaboratorDTO> getCollaboratorById(@PathVariable Long id)
    {
        Optional<CollaboratorDTO> collaboratorDTO = collaborateurService.getById(id);
        return  collaboratorDTO.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

  @GetMapping("/currentUser")
    @CrossOrigin(origins = "http://localhost:3000")
    public Collaborateur profile(Principal principal){
        //Principal utilsateur username
        return collaborateurService.laodUserByUserName(principal.getName());

    }

    @PutMapping("/updateCollaborator/{id}")
    public ResponseEntity<HttpResponse> collaboratorUpdate(@PathVariable("id") Long id, @RequestBody LightCollaboratorDTO collab) {
        LightCollaboratorDTO updatedUser = collaborateurService.updateCollab(id, collab);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("updatedUser", updatedUser))
                        .message("User account updated successfully!")
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PutMapping("/updateCurrentCollab")
    public ResponseEntity<HttpResponse> updateCurrentUser(@RequestBody CollaboratorDTO collab) {
        Collaborateur updatedCollab = collaborateurService.updateLoggedUser(collab);

        if (updatedCollab == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    HttpResponse.builder()
                            .timestamp(now().toString())
                            .message("User is not enabled")
                            .status(HttpStatus.FORBIDDEN)
                            .build()
            );
        }
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("updatedCollaborator", updatedCollab))
                        .message("Logged Collaborator Updated")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PutMapping(value="/assignRole/{userId}/{roleId}" )
    public  ResponseEntity<Void> assignRoleToCollaborator(@PathVariable Long userId , @PathVariable Long roleId ){
            roleService.assignRoleToCollaborator(userId, roleId);
            return ResponseEntity.ok().build();
    }

    @GetMapping("/fetchRolesForCollab/{collaboratorId}")
    @PreAuthorize("hasRole('ROLE_RH')")
    public List<Role> getAvailableRolesForCollaborator(@PathVariable Long collaboratorId) {
        return roleService.fetchAvailableRole(collaboratorId);
    }

    @GetMapping("/fetchUserCollab/{collaboratorId}")
    public Set<Role> getUserCurrentRole(@PathVariable Long collaboratorId) {
        return roleService.fetchUserAvailableRole(collaboratorId);
    }

    @PutMapping("/removeRole/{userId}/{roleId}")
    @PreAuthorize("hasRole('ROLE_RH')")
    public void removeRoleFromCollaborator(@PathVariable Long userId, @PathVariable Long roleId) {
        roleService.removeRoleFromCollaborator(userId,roleId);

        log.info("Role with ID: {} has been removed from user with ID: {}", roleId, userId);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollaborateur(@PathVariable Long id ) {
        collaborateurService.remove(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/roleManager")
    public ResponseEntity<HttpResponse> getCollaboratorsWithRoleManager(
                                                 @RequestParam Optional<Integer> page,
                                                 @RequestParam Optional<Integer> size)   {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page",collaborateurService.ListCollaboratorsManager(page.orElse(0) ,size.orElse(10))))
                        .message("Collaborators with role Manager retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }

    @GetMapping("Equipe/role-manager")
    public ResponseEntity<HttpResponse> getCollaboratorsWithRoleManagerWithoutEquipe(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size)   {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page",collaborateurService.ListCollaboratorsManagerWithOutEquipe(page.orElse(0) ,size.orElse(10))))
                        .message("Collaborators with role Manager retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }
    @GetMapping("fetchResponsibleAvailable/roleManager")
    public ResponseEntity<HttpResponse> fetchResponsibleAvailable(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size)   {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("",collaborateurService.FetchResponsiblesAvailable(page.orElse(0) ,size.orElse(10))))
                        .message("Responsibles  retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }

    @GetMapping("/role-collaborator")
    public ResponseEntity<HttpResponse> getCollaboratorsWithRoleCollaborators(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size)   {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page",collaborateurService.ListCollaboratorsCollaborator(page.orElse(0) ,size.orElse(10))))
                        .message("Collaborators with role Collaborators retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }

    @GetMapping("/fetchRoleCollab")
    public ResponseEntity<HttpResponse> getOnlyCollaborators(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size)   {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("page",collaborateurService.FetchCollaborators(page.orElse(0) ,size.orElse(10))))
                        .message("Collaborators with role Collaborators retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }


    @GetMapping("/getCollaboratorsByTeam/{idEquipe}")
    public ResponseEntity<HttpResponse> getCollaboratorsByTeam(
            @PathVariable Long idEquipe)
            {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("List",collaborateurService.findCollaboratorsByTeam(idEquipe)))
                        .message(" Collaborators Retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }


    @PatchMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request, Principal connectedUser) {
        collaborateurService.changePassword(request, connectedUser);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully.");
        return ResponseEntity.ok(response);
    }



}
