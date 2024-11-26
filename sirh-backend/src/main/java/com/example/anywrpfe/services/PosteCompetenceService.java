package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.PosteCompetenceDTO;
import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.entities.Poste;
import com.example.anywrpfe.entities.PosteCompetence;
import jakarta.transaction.Transactional;

import java.util.List;

public interface PosteCompetenceService {


    @Transactional
    Poste addCompetenceToPoste(Long posteId , Long competenceId , String evaluation);

    PosteCompetence getById(Long id);
    List<PosteCompetence> getAllPosts();


    PosteCompetenceDTO updateCompetencePosteEvaluation(Long posteId, Long competenceId, String newEvaluation, TypeEval newScaleType);

    List<PosteCompetence> getPosteByCompetence(Long competenceId);

    List<PosteCompetence> getPosteCompetence(Long posteId);

    List<PosteCompetence> getCompetencesByType(Long posteId, TypeEval scaleType);
}
