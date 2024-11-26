package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Poste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosteRepository extends JpaRepository<Poste,Long> {

    Poste findByIdPoste(Long id);

    boolean existsByTitre(String titre);
}
