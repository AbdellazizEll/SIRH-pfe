package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.entities.PosteCompetence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PosteCompetenceRepository  extends JpaRepository<PosteCompetence,Long> {

    List<PosteCompetence> findByPoste_IdPoste(Long posteId);

    List<PosteCompetence> findAll();

    List<PosteCompetence> findByCompetence_Id(Long id);


    Optional<PosteCompetence> findByPoste_IdPosteAndCompetence_Id(Long collaborateurId, Long competenceId);

    List<PosteCompetence> findByPoste_IdPosteAndScaleType(Long collaborateurId, TypeEval scaleType);


}
