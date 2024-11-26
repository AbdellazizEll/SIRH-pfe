package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Catalogue;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LightCatalogueDTO {


    private Long id;
    private String title;
    private String description;


    public static LightCatalogueDTO fromEntity(Catalogue catalogue) {
        if (catalogue == null) {
            return null;
        }

        return LightCatalogueDTO.builder()
                .id(catalogue.getId())
                .title(catalogue.getTitle())
                .description(catalogue.getDescription())
                .build();
    }

    public static Catalogue toEntity(LightCatalogueDTO catalogueDTO) {
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
