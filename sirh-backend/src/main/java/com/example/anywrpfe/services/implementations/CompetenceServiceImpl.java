package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.dto.CompetenceDTO;
import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.Enum.TypeEval;
import com.example.anywrpfe.repositories.CompetenceRepository;
import com.example.anywrpfe.services.CompetenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CompetenceServiceImpl implements CompetenceService {

    private final CompetenceRepository competenceRepository;
    @Override
    public Competence createCompetence(CompetenceDTO data) {
        Competence competence = new Competence();
        competence.setName(data.getName());
        competence.setDescription(data.getDescription());
        competence.setScaleType(data.getScaleType());

        if (data.getPossibleValues() == null || data.getPossibleValues().isEmpty()) {
            competence.setDefaultValuesForScaleType(data.getScaleType());
        } else {
            competence.setPossibleValues(data.getPossibleValues());
        }

        return competenceRepository.save(competence);
    }


    @Override
    public Competence updateCompetence(Long id, Competence competenceDetails) {

        Competence competence = competenceRepository.findById(id).orElseThrow(() -> new RuntimeException("Competence Not found "));
        competence.setName(competenceDetails.getName());
        competence.setDescription(competenceDetails.getDescription());
        competence.setScaleType(competenceDetails.getScaleType());

        if (competenceDetails.getPossibleValues().isEmpty()) {
            competence.setDefaultValuesForScaleType(competenceDetails.getScaleType());
        } else {
            competence.setPossibleValues(competenceDetails.getPossibleValues());
        }

        return competenceRepository.save(competence);
    }

    @Override
    public Competence updateCompetenceScale(Long id , TypeEval scaleType)
    {
        Competence competence = competenceRepository.findById(id).orElseThrow(()-> new RuntimeException("Competence not found"));

        TypeEval typeEval = competenceRepository.findByScaleType(scaleType);
        competence.setScaleType(typeEval);
        return competenceRepository.save(competence);
    }

    @Override
    public void removeCompetence(Long idCompetence) {
        competenceRepository.deleteById(idCompetence);

    }

    @Override
    public Page<CompetenceDTO> getAllCompetences(String name, int page, int size) {
        // Fetch the paged list of Competence entities from the repository
        Page<Competence> competencePage = competenceRepository.findAllByNameContaining(name, PageRequest.of(page, size));

        // Convert each Competence to CompetenceDTO using map function
       return competencePage.map(CompetenceDTO::fromEntity);

    }

    @Override
    public Optional<Competence> getCompetenceById(Long competenceId) {
        return competenceRepository.findById(competenceId);
    }

    @Override
    public List<String> fetchPossibleValuesOfCompetence(Long competenceId) {

        Competence competence = competenceRepository.findById(competenceId).orElseThrow(() -> new RuntimeException("Not found competence"));

        List<String> possibleValues = competence.getPossibleValues().stream().toList();
        log.info("Possible Values of competence {} : ",possibleValues, competenceId);

        return possibleValues;
    }
}
