package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.CollaboratorCompetenceGrowthDTO;
import com.example.anywrpfe.entities.CompetenceHistory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompetenceHistoryService {
     List<CompetenceHistory> getCompetenceHistoryForCollaborator(Long collaboratorId);

    List<CollaboratorCompetenceGrowthDTO> findCollaboratorWithHighestCompetenceGrowth(Pageable pageable);
}
