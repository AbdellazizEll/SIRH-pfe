package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.dto.CollaboratorCompetenceGrowthDTO;
import com.example.anywrpfe.dto.LightCollaboratorDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.CompetenceHistory;
import com.example.anywrpfe.repositories.CompetenceHistoryRepository;
import com.example.anywrpfe.services.CompetenceHistoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompetenceHistoryServiceImpl implements CompetenceHistoryService {
    private final CompetenceHistoryRepository competenceHistoryRepository;

    public List<CompetenceHistory> getCompetenceHistoryForCollaborator(Long collaboratorId) {
        return competenceHistoryRepository.findByCollaboratorId(collaboratorId);
    }


    @Override
    public List<CollaboratorCompetenceGrowthDTO> findCollaboratorWithHighestCompetenceGrowth(Pageable pageable) {
        // Fetch competence history based on pageable
        List<CompetenceHistory> histories = competenceHistoryRepository.findAllCompetenceHistory(pageable);
        Map<Collaborateur, Integer> growthMap = new HashMap<>();

        for (CompetenceHistory history : histories) {
            int growth = history.getNumericNewEvaluation() - history.getNumericOldEvaluation();
            Collaborateur collaborator = history.getCollaborator();

            // Accumulate growth for each collaborator
            growthMap.put(collaborator, growthMap.getOrDefault(collaborator, 0) + growth);
        }

        // Sort collaborators by their growth and convert to DTO
        return growthMap.entrySet().stream()
                .map(entry -> new CollaboratorCompetenceGrowthDTO(LightCollaboratorDTO.fromEntity(entry.getKey()), entry.getValue()))
                .sorted(Comparator.comparingInt(CollaboratorCompetenceGrowthDTO::getGrowth).reversed())
                .collect(Collectors.toList());
    }
}
