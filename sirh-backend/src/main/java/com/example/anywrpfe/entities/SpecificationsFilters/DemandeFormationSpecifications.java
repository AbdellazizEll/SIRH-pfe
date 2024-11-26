package com.example.anywrpfe.entities.SpecificationsFilters;

import com.example.anywrpfe.entities.DemandeFormation;
import com.example.anywrpfe.entities.Enum.StatusType;
import org.springframework.data.jpa.domain.Specification;

public class DemandeFormationSpecifications {

    public static Specification<DemandeFormation> hasStatus(StatusType status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    // Specification to filter by targetCompetence
    public static Specification<DemandeFormation> hasTargetCompetence(Long competenceId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("formation").get("targetCompetence").get("id"), competenceId);
    }

    public static Specification<DemandeFormation> hasManagerId(Long managerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("manager").get("id"), managerId);
    }
}
