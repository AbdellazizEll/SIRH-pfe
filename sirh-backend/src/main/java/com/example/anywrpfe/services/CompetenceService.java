package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.CompetenceDTO;
import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.Enum.TypeEval;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CompetenceService {

    Competence createCompetence(CompetenceDTO data);


    Competence updateCompetence(Long id , Competence competenceDetails);

    Competence updateCompetenceScale(Long id, TypeEval scaleType);

    void  removeCompetence(Long idCompetence);

    Page<CompetenceDTO> getAllCompetences(String name, int page , int size);

    Optional<Competence> getCompetenceById(Long competenceId);

     List<String> fetchPossibleValuesOfCompetence(Long competenceId);

}
