package com.example.anywrpfe.repositories;

import com.example.anywrpfe.dto.CompetenceCoverageByDepartmentDTO;
import com.example.anywrpfe.dto.CompetenceGapDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.CollaborateurCompetence;
import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.Enum.TypeEval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollaborateurCompetenceRepository extends JpaRepository<CollaborateurCompetence,Long> {

    List<CollaborateurCompetence> findByCollaborateurId(Long collaborateurId);

    List<CollaborateurCompetence> findByCompetenceId(Long competenceId);

    Optional<CollaborateurCompetence> findByCollaborateurIdAndCompetenceId(Long collaborateurId, Long competenceId);

    CollaborateurCompetence findByCollaborateurAndCompetence(Collaborateur collaborateur , Competence competence);
    List<CollaborateurCompetence> findByCollaborateurIdAndScaleType(Long collaborateurId, TypeEval scaleType);

    @Query("SELECT cc FROM CollaborateurCompetence cc " +
            "JOIN cc.competence c " +
            "JOIN cc.collaborateur coll " +
            "JOIN coll.posteOccupe p " +
            "JOIN p.posteCompetences pc " +
            "WHERE pc.competence = cc.competence AND coll.id = :collaborateurId")
    List<CollaborateurCompetence> getCollaboratorCompetences(@Param("collaborateurId") Long collaborateurId);

    @Query("SELECT new com.example.anywrpfe.dto.CompetenceGapDTO(c.id, c.name, pc.evaluation, cc.evaluation) " +
            "FROM CollaborateurCompetence cc " +
            "JOIN cc.competence c " +
            "JOIN cc.collaborateur coll " +
            "JOIN coll.posteOccupe p " +
            "JOIN p.posteCompetences pc " +
            "WHERE pc.competence = cc.competence AND coll.id = :collaborateurId " +
            "AND pc.evaluation > cc.evaluation " + // Only get competencies where there's a gap
            "GROUP BY c.id, c.name, pc.evaluation, cc.evaluation")
    List<CompetenceGapDTO> getCompetenceGap(@Param("collaborateurId") Long collaborateurId);

}
