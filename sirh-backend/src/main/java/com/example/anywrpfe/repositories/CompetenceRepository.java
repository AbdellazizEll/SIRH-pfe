package com.example.anywrpfe.repositories;

import com.example.anywrpfe.dto.CompetenceGapDTO;
import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.Enum.TypeEval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompetenceRepository extends JpaRepository<Competence,Long> {

    TypeEval findByScaleType(TypeEval typeEval);

    Page<Competence> findAllByNameContaining(String name, Pageable pageable);



    @Query("SELECT new com.example.anywrpfe.dto.CompetenceGapDTO(c.id, c.name, (pc.evaluation - cc.evaluation)) " +
            "FROM CollaborateurCompetence cc " +
            "JOIN cc.competence c " +
            "JOIN cc.collaborateur coll " +
            "JOIN coll.posteOccupe p " +
            "JOIN p.posteCompetences pc " +
            "WHERE pc.competence = cc.competence AND coll.id = :collaborateurId " +
            "AND cc.evaluation < pc.evaluation")
    List<CompetenceGapDTO> getCompetenceGap(@Param("collaborateurId") Long collaborateurId);

}
