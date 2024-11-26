package com.example.anywrpfe.services;


import com.example.anywrpfe.auth.ChangePasswordRequest;
import com.example.anywrpfe.dto.CollaboratorDTO;
import com.example.anywrpfe.dto.CollaboratorTrainingStatsDTO;
import com.example.anywrpfe.dto.LightCollaboratorDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Enum.ManagerType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface CollaborateurService {



    Collaborateur create(Collaborateur data);

    Page<LightCollaboratorDTO> ListCollaborators(String name, int page , int size);


   Optional <CollaboratorDTO> getById(Long id);


    void changePassword(ChangePasswordRequest request , Principal userconnected);



     Collaborateur laodUserByUserName(String email);


     LightCollaboratorDTO updateCollab(Long id, LightCollaboratorDTO collab);

     Collaborateur updateLoggedUser(@RequestBody CollaboratorDTO collaborateur);


    List<CollaboratorDTO> findEquipeManagers();

    List<LightCollaboratorDTO> findEquipeManagersAvailable();

    @Transactional
    void unAssignManagerFromEquipe(Long equipeId, Long collaboratorId);

    List<CollaboratorDTO> findDepartmentResponsibles();

    @Transactional
    void unAssignDepartmentResponsible(Long departmentId, Long collaboratorId);

    List<CollaboratorDTO> findResponsibleWithoutDepartments();

    List<LightCollaboratorDTO> findJoblessCollaborators();

    List<LightCollaboratorDTO> findTeamlessCollaborators();

    void updateManagerType(Long collaborateurId, ManagerType newType);


 Boolean remove(@PathVariable Long id);


//    void assignCollaboratorToDepartment(Long collaboratorId, Long departmentId);



    Page<LightCollaboratorDTO> ListCollaboratorsManager(int page , int size);

    Page<CollaboratorDTO>FetchResponsiblesAvailable(int page, int size);

    Page<CollaboratorDTO>ListCollaboratorsManagerWithOutEquipe(int page, int size);

    Page<LightCollaboratorDTO> ListCollaboratorsCollaborator(int page , int size);

    Page<LightCollaboratorDTO> FetchCollaborators(int page, int size);

    CollaboratorDTO assignPosteToCollaborateur(Long collaborateurId, Long posteId);
    CollaboratorDTO removePosteFromCollaborateur(Long collaborateurId);


    List<LightCollaboratorDTO> findCollaboratorsByTeam(Long idEquipe);



 CollaboratorTrainingStatsDTO getCollaboratorWithHighestTrainingCompletion();
}
