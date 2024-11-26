package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.CompetenceHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompetenceHistoryRepository extends JpaRepository<CompetenceHistory,Long> {
    List<CompetenceHistory> findByCollaboratorId(Long collaboratorId);

    List<CompetenceHistory> findByCollaborator_IdAndCompetence_Id(Long collaboratorId, Long competenceId);

    @Query("SELECT ch FROM CompetenceHistory ch")
    List<CompetenceHistory> findAllCompetenceHistory(Pageable pageable);

    // Checks for a specific evaluation change to avoid duplication for successful completions
    boolean existsByCollaboratorAndCompetenceAndOldEvaluationAndNewEvaluation(
            Collaborateur collaborator, Competence competence, String oldEvaluation, String newEvaluation);

    // Checks only for the presence of a "Failed" evaluation in the history
    boolean existsByCollaboratorAndCompetenceAndOldEvaluation(
            Collaborateur collaborator, Competence competence, String oldEvaluation);

}
