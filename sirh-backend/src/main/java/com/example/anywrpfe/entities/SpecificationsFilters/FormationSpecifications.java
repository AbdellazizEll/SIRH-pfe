package com.example.anywrpfe.entities.SpecificationsFilters;

import com.example.anywrpfe.entities.Formation;
import org.springframework.data.jpa.domain.Specification;

public class FormationSpecifications {

    public static Specification<Formation> titleContains(String query) {
        return (root, queryCriteria, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + query.toLowerCase() + "%");
    }

    public static Specification<Formation> hasCompetenceId(Long competenceId) {
        return (root, queryCriteria, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("targetCompetence").get("id"), competenceId);
    }

    public static Specification<Formation> inCatalogueId(Long catalogueId) {
        return (root, queryCriteria, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("catalogue").get("id"), catalogueId);
    }
}
