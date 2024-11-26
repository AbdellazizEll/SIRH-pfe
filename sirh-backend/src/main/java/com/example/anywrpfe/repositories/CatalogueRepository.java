package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogueRepository extends JpaRepository<Catalogue,Long> {

    Optional<Catalogue> findById(Long id);

}
