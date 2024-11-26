package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Departement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement,Long> {

    Page<Departement> findByNomDepContaining(String name, Pageable pageable);
    Optional<Departement> findByResponsible(Collaborateur responsible);

}
