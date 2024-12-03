package com.example.anywrpfe.services.implementations;


import com.example.anywrpfe.dto.CatalogueDTO;
import com.example.anywrpfe.entities.Catalogue;
import com.example.anywrpfe.repositories.CatalogueRepository;
import com.example.anywrpfe.services.CatalogueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogueServiceImpl implements CatalogueService {

    private final CatalogueRepository catalogueRepository;

    @Override
    public List<CatalogueDTO> getAllCatalogues() {
        List<Catalogue> catalogueList = catalogueRepository.findAll();
        return catalogueList.stream().map(CatalogueDTO::fromEntity).toList();
    }

    @Override
    public Catalogue addCatalogue(CatalogueDTO data) {

        Catalogue cat = new Catalogue();
        cat.setTitle(data.getTitle());
        cat.setDescription(data.getDescription());

        return catalogueRepository.save(cat);
    }


    @Override
    public Catalogue getCatalogueById(Long id){
        return catalogueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catalogue not found with ID: " + id));    }

    @Override
    public Catalogue updateCatalogue(Long id, CatalogueDTO catalogueDTO) {

        Catalogue catalogue = catalogueRepository.findById(id).orElseThrow(() -> new RuntimeException("Competence Not found "));
        catalogue.setTitle(catalogue.getTitle());
        catalogue.setDescription(catalogue.getDescription());

        return catalogueRepository.save(catalogue);
    }

    @Override
    public Catalogue modifierCatalogue(Long id,Catalogue catalogue) {
        Catalogue cat = catalogueRepository.findById(id).orElseThrow(()-> new RuntimeException("Catalogue not found"));

        cat.setTitle(catalogue.getTitle());
        cat.setDescription(catalogue.getDescription());

        return catalogueRepository.save(cat);
    }

    @Override
    public void deleteCatalogue(Long id) {
        Catalogue catalogue = catalogueRepository.findById(id).orElseThrow(()-> new RuntimeException("Catalogue not found"));
        catalogueRepository.delete(catalogue);
    }
}
