package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Formation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FormationRepository extends JpaRepository<Formation,Long> {

    Page<Formation> findByCatalogueId(Long catalogueId , Pageable pageable);

    Page<Formation> findByTargetCompetenceId(Long competenceId,Pageable pageable);
    Page<Formation> findByTargetCompetenceIdAndTargetCompetenceName(Long competenceId,String name, Pageable pageable);

    Optional<Formation> findById(Long id); // Spring Data JPA provides this method by default

    @Query("SELECT f FROM Formation f WHERE f.targetCompetence.id = :competenceId AND f.currentLevel >= :currentLevel AND f.targetLevel <= :targetLevel")
    List<Formation> findFormationByTargetCompetenceAndLevelRange(@Param("competenceId") Long competenceId,
                                                                 @Param("currentLevel") int currentLevel,
                                                                 @Param("targetLevel") int targetLevel);

    Page<Formation> findAll(Specification<Formation> spec, Pageable pageable);
}
