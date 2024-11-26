package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.FormationDetailDTO;
import com.example.anywrpfe.dto.PosteDTO;
import com.example.anywrpfe.entities.CompetenceComparisonResult;
import com.example.anywrpfe.entities.Poste;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;


public interface PosteService {



     Poste createPoste(PosteDTO posteDTO);

     Optional<PosteDTO> getPosteById(Long id);


     PosteDTO updatePoste(Long posteId , PosteDTO posteDTO);



     void removePoste(Long id);

     List<PosteDTO> findAll();

    CompetenceComparisonResult compareCollaboratorToPoste(Long collaborateurId, Long posteId);



    List<FormationDetailDTO> getTeamSuggestedTrainings(Long managerId, Long competenceId, Integer currentLevel, Integer targetLevel);

    List<FormationDetailDTO> getFilteredSuggestedTrainings(Long collaborateurId, Long posteId, Long competenceId, Integer currentLevel, Integer targetLevel);

    @Transactional
    void unassignCollaboratorFromPoste(Long posteId, Long collaboratorId);
}
