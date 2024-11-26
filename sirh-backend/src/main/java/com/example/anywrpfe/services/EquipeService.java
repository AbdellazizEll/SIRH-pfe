package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.EquipeDTO;
import com.example.anywrpfe.entities.Equipe;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface EquipeService {

     Equipe createEquipe(EquipeDTO equipe);

    @Transactional
    Equipe createStaticEquipeDepartment(EquipeDTO equipe, Long depId);

     Equipe updateEquipe(Long idEquipe, EquipeDTO equipe);

    Page<EquipeDTO> getAllEquipes(String name, int page , int size);


    Optional<EquipeDTO> findEquipeByManagerId(Long idManager);

    Optional<EquipeDTO> findEquipeById(@PathVariable Long id);




    @Transactional
    void removeCollabFromEquipe(@PathVariable Long id, Long equipeId);


    @Transactional
    void setCollaboratorToEquipe(Long collaboratorId , Long equipeId);

    @Transactional
    void setListofCollabs(List<Long> collaboratorId, Long equipeId);

    @Transactional
    void setManagerToEquipe(Long collaboratorId , Long equipeId);

    @Transactional
    Equipe assignManagerToEquipe(Long idManager, Long idEquipe);

    void unassignCollaboratorFromEquipe(Long equipeId, Long collaboratorId);

    @Transactional
    void unassignManagerFromEquipe(Long equipeId, Long collaboratorId);

    @Transactional
    void deleteTeam(Long teamId);

    @Transactional
    Equipe assignEquipeToDepartment(Long idEquipe, Long idDep);

}
