package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Inscription;
import com.example.anywrpfe.entities.Formation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollementRepository extends JpaRepository<Inscription, Long> {

    Inscription findByCollaborator_IdAndFormationId(Long collaboratorId, Long formationId);

    Page<Inscription> findByCollaborator_Id(Long collaboratorId, Pageable pageable);

    int countByCollaborator_IdAndCompletedTrue(Long collaboratorId);

    int countByCollaborator_Id(Long collaboratorId);

    List<Inscription> findByCollaborator_Id(Long collaboratorId);

    // 1. Total enrollments
    @Query("SELECT COUNT(e) FROM Inscription e")
    long countTotalEnrollments();

    // 2. Completed trainings count
    @Query("SELECT COUNT(e) FROM Inscription e WHERE e.completed = true")
    long countCompletedTrainings();

    // 3. Average progress across all enrollments
    @Query("SELECT AVG(e.progress) FROM Inscription e")
    Double getAverageProgress();

    // 4. Overall competence improvement rate (adjusted to use progress)
    @Query("SELECT AVG(e.progress) FROM Inscription e WHERE e.completed = true")
    Double getOverallCompetenceImprovementRate();

    boolean existsByCollaboratorAndFormation(Collaborateur employee, Formation formation);

    boolean existsByFormationIdAndCollaboratorId(Long id, Long id1);

    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.collaborator = :collaborator")
    int countByCollaborator(@Param("collaborator") Collaborateur collaborator);

    @Query("SELECT AVG(e.progress) FROM Inscription e WHERE e.completed = true AND e.collaborator.equipe.departement.id_dep = :departmentId")
    Double getOverallCompetenceImprovementRateByDepartment(@Param("departmentId") Long departmentId);
}
