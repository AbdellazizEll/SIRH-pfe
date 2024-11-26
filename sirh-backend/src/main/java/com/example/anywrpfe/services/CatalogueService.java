package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.CatalogueDTO;
import com.example.anywrpfe.entities.Catalogue;

import java.util.List;

public interface CatalogueService {
     List<CatalogueDTO> getAllCatalogues();

     Catalogue addCatalogue(CatalogueDTO catalogue);

    Catalogue getCatalogueById(Long id);

    Catalogue updateCatalogue(Long id , CatalogueDTO catalogueDTO);

    Catalogue modifierCatalogue(Long id, Catalogue catalogue);
     void deleteCatalogue(Long id);
}
