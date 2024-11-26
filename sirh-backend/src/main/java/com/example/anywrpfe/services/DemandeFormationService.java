package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.DemandeFormationDTO;
import com.example.anywrpfe.dto.FormationDTO;
import com.example.anywrpfe.entities.DemandeFormation;
import com.example.anywrpfe.entities.Enum.StatusType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

public interface DemandeFormationService {


     Page<DemandeFormationDTO> getAllRequests(StatusType status, Long competenceId, int page, int size);



    Page<DemandeFormationDTO> getManagerTrainingRequests(Long managerId,StatusType status, Long competenceId, int page, int size);

    @Transactional
     DemandeFormation addRequest(DemandeFormationDTO request);

    @Transactional
    DemandeFormation addCustomizedRequest(DemandeFormationDTO request, FormationDTO customFormationDTO);

     void approveRequest(Long id);

     void rejectRequest(Long id,String rejectionReason);

     DemandeFormationDTO getById(Long id);


    @Transactional
     void deleteRequest(Long id );

    boolean assignCollaboratorToTraining(Long collaboratorId, Long formationId);

    @Transactional
    DemandeFormation createRequestFromSuggestedTraining(Long formationId, Long managerId, Long collaboratorId);
}
