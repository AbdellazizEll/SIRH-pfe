package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Catalogue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class CatalogueDTO {

    private Long id;
    private String title;
    private String description;

    // Prevents circular reference by avoiding fetching formations unless needed
    @JsonIgnoreProperties("catalogue")
    private List<FormationDTO> formations;

    public static CatalogueDTO fromEntity(Catalogue catalogue) {
        if (catalogue == null) {
            return null;
        }

        return CatalogueDTO.builder()
                .id(catalogue.getId())
                .title(catalogue.getTitle())
                .description(catalogue.getDescription())
                .formations(catalogue.getFormations() != null ?
                        catalogue.getFormations().stream()
                                .map(FormationDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public static Catalogue toEntity(CatalogueDTO catalogueDTO) {
        if (catalogueDTO == null) {
            return null;
        }

        Catalogue catalogue = new Catalogue();
        catalogue.setId(catalogueDTO.getId());
        catalogue.setTitle(catalogueDTO.getTitle());
        catalogue.setDescription(catalogueDTO.getDescription());

        return catalogue;
    }
}
