package com.example.anywrpfe.controller;

import com.example.anywrpfe.auth.response.HttpResponse;
import com.example.anywrpfe.dto.CatalogueDTO;
import com.example.anywrpfe.entities.Catalogue;
import com.example.anywrpfe.services.CatalogueService;
import com.example.anywrpfe.services.FormationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static java.time.LocalTime.now;
import static javax.security.auth.callback.ConfirmationCallback.OK;

@RequestMapping("/catalogue")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CatalogueController {

    private final FormationService formationService;
    private final CatalogueService catalogueService;
    ///CATALOGUE
    @GetMapping("/getAllCatalogues")
    public ResponseEntity<HttpResponse> getAllCatalogues() {

        log.info("Fetching Catalogues");
        List<CatalogueDTO> catalogues = catalogueService.getAllCatalogues();

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("catalogues",catalogues))
                        .message("Liste des Catalogues ")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }
    @GetMapping("/{catalogueId}")
    public ResponseEntity<CatalogueDTO> getByCatalogueId(@PathVariable Long catalogueId) {
        try {
            log.info("Fetching Catalogue by ID: {}", catalogueId);
            Catalogue catalogue = catalogueService.getCatalogueById(catalogueId);
            log.info("Catalogue fetched: {}", catalogue);

            // Convert to DTO
            CatalogueDTO dto = CatalogueDTO.fromEntity(catalogue);
            log.info("Catalogue DTO: {}", dto);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching catalogue: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createCatalogue")
    public ResponseEntity<Catalogue> addCatalogue(@RequestBody CatalogueDTO catalogue) {

        Catalogue catalogue1 = catalogueService.addCatalogue(catalogue);
        return new ResponseEntity<>(catalogue1, HttpStatus.CREATED);


    }

    @PutMapping("/affect/{idCatalogue}/{idFormation}")
    public ResponseEntity<HttpResponse> affectFormation(@PathVariable Long  idCatalogue , @PathVariable Long idFormation ) {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("",formationService.affectFormationToCatalogue(idCatalogue,idFormation)))
                        .message("Formation a été affecté ")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HttpResponse> update(@PathVariable Long  id , @RequestBody CatalogueDTO catalogueDTO ) {

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(Map.of("",catalogueService.updateCatalogue(id,catalogueDTO)))
                        .message("Formation a été affecté ")
                        .status(HttpStatus.OK)
                        .statusCode(OK)
                        .build());

    }
    @DeleteMapping("deleteCatalogue/{id}")
    public ResponseEntity<Void> deleteCatalogue(@PathVariable Long id) {

        log.info("Deleting Catalogue");
        catalogueService.deleteCatalogue(id);
        return ResponseEntity.ok().build();
    }
    /// END CATALOGUE
}
