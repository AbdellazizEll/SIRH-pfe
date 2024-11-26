package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.auth.exception.absenceExceptions.AbsenceExceptions;
import com.example.anywrpfe.auth.exception.formationExceptions.FormationException;
import com.example.anywrpfe.dto.FormationDTO;
import com.example.anywrpfe.entities.SpecificationsFilters.FormationSpecifications;
import com.example.anywrpfe.entities.Catalogue;
import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.Formation;
import com.example.anywrpfe.repositories.CatalogueRepository;
import com.example.anywrpfe.repositories.CompetenceRepository;
import com.example.anywrpfe.repositories.FormationRepository;
import com.example.anywrpfe.services.FormationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class FormationServiceImpl  implements FormationService {
    private final CompetenceRepository competenceRepository;
    private final CatalogueRepository catalogueRepository;
    private final FormationRepository formationRepository;

    @Override
    // FormationService.java
    public Page<FormationDTO> getAllFormations(String query, Long competenceId, Long catalogueId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Formation> spec = Specification.where(null);

        if (query != null && !query.isEmpty()) {
            spec = spec.and(FormationSpecifications.titleContains(query));
        }
        if (competenceId != null) {
            spec = spec.and(FormationSpecifications.hasCompetenceId(competenceId));
        }
        if (catalogueId != null) {
            spec = spec.and(FormationSpecifications.inCatalogueId(catalogueId));
        }

        Page<Formation> formationsPage = formationRepository.findAll(spec, pageable);
        return formationsPage.map(FormationDTO::fromEntity);
    }


    @Override
    public Page<FormationDTO> getFormationsByCatalogue(Long idCatalogue, Pageable pageable){

        // Fetch paginated formations by catalogue ID and map them to FormationDTO
        return formationRepository.findByCatalogueId(idCatalogue, pageable)
                .map(FormationDTO::fromEntity);
    }

    @Override
    public Page<Formation> getFormationByTargetCompetence(Long idCompetence, String name, int page, int size)
    {

        return formationRepository.findByTargetCompetenceIdAndTargetCompetenceName(idCompetence,name,PageRequest.of(page,size));

    }

    @Override
    public Page<Formation> getFormationByTargetCompetenceId(Long idCompetence, int page, int size)
    {

        return formationRepository.findByTargetCompetenceId(idCompetence,PageRequest.of(page,size));

    }


    @Override
    public Formation addFormation(FormationDTO data)

    {
        Formation formation = new Formation();
        formation.setTitle(data.getTitle());
        formation.setDescription(data.getDescription());
        // Fetch the full Competence entity using the id from the DTO
        Competence competence = competenceRepository.findById(data.getTargetCompetence().getId())
                .orElseThrow(() -> new RuntimeException("Competence not found with id: " + data.getTargetCompetence().getId()));
        formation.setTargetCompetence(competence);

        formation.setCurrentLevel(data.getCurrentLevel());
        formation.setTargetLevel(data.getTargetLevel());
        formation.setStartingAt(data.getStartingAt());
        formation.setFinishingAt(data.getFinishingAt());
        Catalogue catalogue = catalogueRepository.findById(data.getCatalogue().getId())
                .orElseThrow(() -> new RuntimeException("Catalogue not found with id: " + data.getCatalogue().getId()));
        formation.setCatalogue(catalogue);
        return formationRepository.save(formation);
    }

    @Override
    public FormationDTO getFormationById(Long id) {
        // Fetch the Formation entity
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new AbsenceExceptions("Formation not found with id: " + id));

        return FormationDTO.fromEntity(formation);
    }
    @Override
    public Formation modifierFormation(Long id, FormationDTO formationDTO) {
// Retrieve the existing formation by ID
        Optional<Formation> existingFormationOptional = formationRepository.findById(id);

        if (existingFormationOptional.isPresent()) {
            Formation existingFormation = existingFormationOptional.get();

            // Update the fields with the values from FormationDTO
            existingFormation.setTitle(formationDTO.getTitle());
            existingFormation.setDescription(formationDTO.getDescription());
            existingFormation.setCurrentLevel(formationDTO.getCurrentLevel());
            existingFormation.setTargetLevel(formationDTO.getTargetLevel());
            existingFormation.setStartingAt(formationDTO.getStartingAt());
            existingFormation.setFinishingAt(formationDTO.getFinishingAt());

            // Update the target competence (if necessary)
            if (formationDTO.getTargetCompetence() != null && formationDTO.getTargetCompetence().getId() != null) {
                Competence targetCompetence = competenceRepository.findById(formationDTO.getTargetCompetence().getId())
                        .orElseThrow(() -> new RuntimeException("Compétence not found"));
                existingFormation.setTargetCompetence(targetCompetence);
            }

            // Update the catalogue (if necessary)
            if (formationDTO.getCatalogue() != null && formationDTO.getCatalogue().getId() != null) {
                Catalogue catalogue = catalogueRepository.findById(formationDTO.getCatalogue().getId())
                        .orElseThrow(() -> new RuntimeException("Catalogue not found"));
                existingFormation.setCatalogue(catalogue);
            }

            // Save the updated formation to the database
            return formationRepository.save(existingFormation);
        } else {
            throw new FormationException("Formation not found with id: " + id);
        }    }


    @Override
    @Transactional
    public Formation affectFormationToCatalogue(Long idFormation, Long idCatalogue)
    {

        Catalogue catalogue = catalogueRepository.findById(idCatalogue).orElseThrow(() -> new RuntimeException("Catalogue with this id is not available"));

        Formation formation = formationRepository.findById(idFormation).orElseThrow(() -> new RuntimeException("Formation with this id is not available"));

        if(catalogue.getFormations().contains(formation))
        {
            throw new EntityNotFoundException("You cannot add the same Training");
        }
        catalogue.getFormations().add(formation);
        formation.setCatalogue(catalogue);

        return formationRepository.save(formation);
    }

    @Override
    @Transactional
    public void removeFormationFromCatalogue(Long idFormation, Long idCatalogue)
    {

        Catalogue catalogue = catalogueRepository.findById(idCatalogue)
                .orElseThrow(() -> new RuntimeException("Catalogue with this id is not available"));

        Formation formation = formationRepository.findById(idFormation)
                .orElseThrow(() -> new RuntimeException("Formation with this id is not available"));

        if(!catalogue.getFormations().contains(formation))
        {
            throw new EntityNotFoundException("Formation not found in the specified Catalogue.");

        }

        catalogue.getFormations().remove(formation);
        formation.setCatalogue(null);
        formationRepository.save(formation);
    }

    @Override
    @Transactional
    public void deleteFormation(Long id) {

        Formation formation = formationRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found"));

        formationRepository.delete(formation);
    }
}
