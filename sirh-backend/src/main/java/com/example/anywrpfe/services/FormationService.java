package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.FormationDTO;
import com.example.anywrpfe.entities.Formation;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FormationService {

     Page<FormationDTO> getAllFormations(String query, Long competenceId, Long catalogueId, int page, int size);

    Page<FormationDTO> getFormationsByCatalogue(Long idCatalogue, Pageable pageable);

    Page<Formation> getFormationByTargetCompetence(Long idCompetence, String name, int page,int size);

    Page<Formation> getFormationByTargetCompetenceId(Long idCompetence, int page, int size);

    Formation addFormation(FormationDTO formation);

    FormationDTO getFormationById(Long id);

     Formation modifierFormation(Long id , FormationDTO formationDTO);

    @Transactional
    Formation affectFormationToCatalogue(Long idFormation, Long idCatalogue);

    @Transactional
    void removeFormationFromCatalogue(Long idFormation, Long idCatalogue);

    @Transactional
    void deleteFormation(Long id);
}
