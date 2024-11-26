package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.DemandeFormation;
import com.example.anywrpfe.entities.Enum.StatusType;
import com.example.anywrpfe.entities.Formation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemandeFormationRepository extends JpaRepository<DemandeFormation,Long> {

    Page<DemandeFormation> findAllByStatus(StatusType status, Pageable pageable);

    // Find requests made by the currently authenticated manager
    @Query("SELECT df FROM DemandeFormation df WHERE df.employee.equipe IS NOT NULL AND df.employee.equipe.managerEquipe IS NOT NULL AND df.employee.equipe.managerEquipe.email = :email")
    Page<DemandeFormation> findByManagerEmail(@Param("email") String email, Pageable pageable);

    Page<DemandeFormation> findByManagerId(Long managerId, Pageable pageable);
    Page<DemandeFormation> findByManagerIdAndStatus(Long managerId,StatusType status, Pageable pageable);

    boolean existsByEmployeeAndFormationAndStatus(Collaborateur employee, Formation formation, String status);

    boolean existsByEmployeeAndFormation(Collaborateur employee, Formation formation);


    boolean existsByFormationIdAndEmployeeId(Long formationId, Long employeeId);

    Page<DemandeFormation> findAll(Specification<DemandeFormation> spec, Pageable pageable);

}
