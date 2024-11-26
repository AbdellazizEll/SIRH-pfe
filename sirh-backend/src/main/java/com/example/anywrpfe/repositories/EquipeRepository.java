package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Equipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipeRepository extends JpaRepository<Equipe,Long> {

    Page<Equipe> findByNomContaining(String name, Pageable pageable);
    boolean existsByNom(String nom);
    Equipe findByManagerEquipeId(Long id);


    @Query("SELECT e FROM Equipe e WHERE e.departement.id_dep = :departmentId")
    List<Equipe> getAllTeamsByDepartment(@Param("departmentId") Long departmentId);






}
