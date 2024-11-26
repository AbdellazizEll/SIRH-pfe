package com.example.anywrpfe.services.implementations;


import com.example.anywrpfe.auth.exception.formationExceptions.FormationException;
import com.example.anywrpfe.dto.PosteCompetenceDTO;
import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.repositories.*;
import com.example.anywrpfe.services.PosteCompetenceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PosteCompetenceServiceImpl implements PosteCompetenceService {


    private final PosteRepository posteRepository;
    private final CompetenceRepository competenceRepository;

    private final PosteCompetenceRepository posteCompetenceRepository;
    @Override
    public Poste addCompetenceToPoste(Long posteId, Long competenceId, String evaluation) {
        log.info("Adding competence {} to poste {}", competenceId, posteId);
        Poste poste = posteRepository.findById(posteId)
                .orElseThrow(() -> new RuntimeException("Poste  not found"));
        Competence competence = competenceRepository.findById(competenceId)
                .orElseThrow(() -> new RuntimeException("Competence not found"));

        // Log the possible values and the evaluation
        log.info("Possible values for competence {}: {}", competenceId, competence.getPossibleValues());
        log.info("Evaluation provided: {}", evaluation);
        // Validate evaluation
        if (!competence.getPossibleValues().contains(evaluation)) {
            throw new FormationException("Invalid evaluation for the given competence");
        }


        boolean competenceExists = poste.getPosteCompetences().stream()
                .anyMatch(cc -> cc.getCompetence().getId().equals(competenceId));
        if( competenceExists)
        {
            throw new FormationException("Cannot add the same competence to same collaborator");
        }

        PosteCompetence posteCompetence = new PosteCompetence();
        posteCompetence.setPoste(poste);
        posteCompetence.setCompetence(competence);
        posteCompetence.setEvaluation(evaluation);

        posteCompetence.setScaleType(competence.getScaleType());

        posteCompetenceRepository.save(posteCompetence);
        poste.getPosteCompetences().add(posteCompetence);
        posteRepository.save(poste);

        return poste;

    }

    @Override
    public PosteCompetence getById(Long id) {
        return posteCompetenceRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found "));
    }

    @Override
    public List<PosteCompetence> getAllPosts() {
        return posteCompetenceRepository.findAll();
    }


    @Override
    @Transactional
    public PosteCompetenceDTO updateCompetencePosteEvaluation(Long posteId, Long competenceId, String newEvaluation, TypeEval newScaleType) {
        PosteCompetence posteCompetence = posteCompetenceRepository
                .findByPoste_IdPosteAndCompetence_Id(posteId, competenceId)
                .orElseThrow(() -> new RuntimeException("Competence not found for this Collaborateur"));

        Competence competence = competenceRepository.findById(competenceId)
                .orElseThrow(() -> new RuntimeException("Competence not found"));

        List<String> possibleValues = competence.getPossibleValues().stream().toList();

        if (!possibleValues.contains(newEvaluation)) {
            throw new RuntimeException("Invalid evaluation value for the given competence and scale type");
        }
        log.info("Possible values for competence {}: {}", competenceId, possibleValues);
        log.info("Evaluation provided: {}", newEvaluation);

        posteCompetence.setEvaluation(newEvaluation);
        posteCompetenceRepository.save(posteCompetence);

        return PosteCompetenceDTO.fromEntity(posteCompetence);
    }


    @Override
    public List<PosteCompetence> getPosteByCompetence(Long competenceId) {
        return posteCompetenceRepository.findByCompetence_Id(competenceId);
    }

    @Override
    public List<PosteCompetence> getPosteCompetence(Long posteId) {

        Poste poste = posteRepository.findById(posteId).orElseThrow(()-> new RuntimeException("Poste inexistant "));

        return poste.getPosteCompetences().stream().toList();

    }
    @Override
    public List<PosteCompetence> getCompetencesByType(Long posteId, TypeEval scaleType) {
        return posteCompetenceRepository.findByPoste_IdPosteAndScaleType(posteId, scaleType);
    }
}
