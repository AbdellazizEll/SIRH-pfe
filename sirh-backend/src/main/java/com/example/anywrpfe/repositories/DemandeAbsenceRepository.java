package com.example.anywrpfe.repositories;

import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.DemandeAbsence;
import com.example.anywrpfe.entities.Enum.Motif;
import com.example.anywrpfe.entities.Enum.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DemandeAbsenceRepository extends JpaRepository<DemandeAbsence,Long> {



    Page<DemandeAbsence> findAll(Pageable pageable);


    Page<DemandeAbsence> findByFromEquipeManagerEquipeId(Long managerId , Pageable pageable);

    @Query("SELECT d FROM DemandeAbsence d WHERE d.collaborateur.id = :collaborateurId AND d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID ")
    List<DemandeAbsence> findApprovedAbsencesByCollaborateurId(@Param("collaborateurId") Long collaborateurId);

    List<DemandeAbsence> findByCollaborateurId(Long id);
    Page<DemandeAbsence> findByCollaborateurEmailAndStatusDemande(String email , Status status, Pageable pageable);



    @Query("select d from DemandeAbsence d where (d.fromDepartment.id_dep = :id_dep or d.fromEquipe.id_equipe in (select e.id_equipe from Equipe e where e.departement.id_dep = :id_dep) or d.collaborateur.id in (select e.managerEquipe.id from Equipe e where e.departement.id_dep = :id_dep)) and d.approvedByManager = com.example.anywrpfe.entities.Enum.Status.ACCEPTED_MANAGER")
    Page<DemandeAbsence> findByDepartmentWithManagersAndApprovedByManager(@Param("id_dep") Long id_dep, Pageable pageable);



    @Query("SELECT da FROM DemandeAbsence da WHERE da.collaborateur.id = :collaborateurId AND da.absence.typeAbs = :absenceType")
    List<DemandeAbsence> findByCollaborateurAndAbsence_TypeAbs(@Param("collaborateurId") Long collaborateurId , @Param("absenceType") Motif absenceType);


    @Query("select d from DemandeAbsence d where d.fromDepartment.nomDep = :nameDep or d.fromEquipe.id_equipe in (select e.id_equipe from Equipe e where e.departement.id_dep = :id_dep) or d.collaborateur.id in (select e.managerEquipe.id from Equipe e where e.departement.nomDep = :nameDep)")
    Page<DemandeAbsence> findByFromDepartment_NomDepContaining(@Param("nameDep") String nameDep, Pageable pageable);



    Page<DemandeAbsence> findByCollaborateurEmail(String email,Pageable pageable);



    @Query("SELECT d FROM DemandeAbsence d WHERE d.fromEquipe.managerEquipe.id = :managerId " +
            "AND (:status IS NULL OR d.statusDemande = :status) " +
            "AND (:absenceType IS NULL OR d.absence.typeAbs = :absenceType) " +
            "AND (:startDate IS NULL OR d.dateDebut >= :startDate) " +
            "AND (:endDate IS NULL OR d.datefin <= :endDate)")
    Page<DemandeAbsence> findByManagerIdWithFilters(
            @Param("managerId") Long managerId,
            @Param("status") Status status,
            @Param("absenceType") Motif absenceType,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );

    @Query("SELECT d FROM DemandeAbsence d WHERE d.fromEquipe.departement.responsible.id = :responsibleId " +
            "AND (:status IS NULL OR d.statusDemande = :status) " +
            "AND (:absenceType IS NULL OR d.absence.typeAbs = :absenceType) " +
            "AND (:startDate IS NULL OR d.dateDebut >= :startDate) " +
            "AND (:endDate IS NULL OR d.datefin <= :endDate)")
    Page<DemandeAbsence> findByResponsibleIdWithFilters(
            @Param("responsibleId") Long responsibleId,
            @Param("status") Status status,
            @Param("absenceType") Motif absenceType,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );

    @Query("SELECT d FROM DemandeAbsence d WHERE " +
            "(:status IS NULL OR d.statusDemande = :status) " +
            "AND (:absenceType IS NULL OR d.absence.typeAbs = :absenceType) " +
            "AND (:startDate IS NULL OR d.dateDebut >= :startDate) " +
            "AND (:endDate IS NULL OR d.datefin <= :endDate) " +
            "AND d.approvedByManager = com.example.anywrpfe.entities.Enum.Status.ACCEPTED_MANAGER " +
            "AND d.approvedByResponsableDep = com.example.anywrpfe.entities.Enum.Status.ACCEPTED_RESPONSABLE")
    Page<DemandeAbsence> findByRHWithFilters(
            @Param("status") Status status,
            @Param("absenceType") Motif absenceType,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );





    @Query("SELECT CASE WHEN COUNT(da) > 0 THEN true ELSE false END " +
            "FROM DemandeAbsence da " +
            "WHERE da.collaborateur.id = :collaborateurId " +
            "AND ((:startDate BETWEEN da.dateDebut AND da.datefin) " +
            "OR (:endDate BETWEEN da.dateDebut AND da.datefin) " +
            "OR (da.dateDebut BETWEEN :startDate AND :endDate))")
    boolean hasOverlappingAbsences(@Param("collaborateurId") Long collaborateurId,
                                   @Param("startDate") Date startDate,
                                   @Param("endDate") Date endDate);
    // Kpis
    @Query("SELECT SUM(d.requestedDays) FROM DemandeAbsence d WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID")
    int getTotalAbsenceDays();

    @Query("SELECT new com.example.anywrpfe.dto.AverageAbsenceDurationDTO(d.fromDepartment.nomDep ,AVG(d.requestedDays))" +
            "FROM DemandeAbsence d WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID " +
            "GROUP BY d.fromDepartment.nomDep")
    List<AverageAbsenceDurationDTO> getAverageAbsenceDurationByDepartment();

    @Query("SELECT COUNT(d.idDemandeAb) * :totalWorkingDays FROM DemandeAbsence d WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID")
    int getTotalWorkingDays(@Param("totalWorkingDays") int totalWorkingDays);



    @Query("SELECT SUM(d.requestedDays) FROM DemandeAbsence d WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID AND d.fromDepartment.id_dep = :departmentId")
    int getTotalAbsenceDaysByDepartment(@Param("departmentId") Long departmentId);

    // Repository Method: Get total working days by department
    @Query("SELECT COUNT(d.idDemandeAb) * :totalWorkingDays FROM DemandeAbsence d WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID AND d.fromDepartment.id_dep = :departmentId")
    int getTotalWorkingDaysByDepartment(@Param("departmentId") Long departmentId, @Param("totalWorkingDays") int totalWorkingDays);
    // Query to get absenteeism by department
    @Query("SELECT new com.example.anywrpfe.dto.AbsenteeismByDepartmentDTO(d.fromDepartment.nomDep, SUM(d.requestedDays)) " +
            "FROM DemandeAbsence d WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID " +
            "GROUP BY d.fromDepartment.nomDep")
    List<AbsenteeismByDepartmentDTO> getAbsenteeismByDepartment();



    @Query("SELECT COUNT(d) FROM DemandeAbsence d WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID")
    Long getTotalValidAbsencesCount();
    @Query("SELECT new com.example.anywrpfe.dto.TopReasonsForAbsenceDTO(" +
            "d.absence.typeAbs, COUNT(d), d.fromDepartment.nomDep) " +
            "FROM DemandeAbsence d " +
            "WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID " +
            "GROUP BY d.absence.typeAbs, d.fromDepartment.nomDep " +
            "ORDER BY COUNT(d) DESC")
    List<TopReasonsForAbsenceDTO> getTopReasonsForAbsenceWithoutPercentage();


    @Query("SELECT d.collaborateur AS collaborator, COUNT(d) AS absenceCount " +
            "FROM DemandeAbsence d " +
            "WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID " +
            "GROUP BY d.collaborateur " +
            "ORDER BY absenceCount DESC")
    List<Object[]> findCollaboratorWithMostAbsences();

    @Query("SELECT d.collaborateur, COUNT(d) FROM DemandeAbsence d " +
            "WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID " +
            "GROUP BY d.collaborateur " +
            "ORDER BY COUNT(d) ASC")
    List<Object[]> findCollaboratorWithLeastAbsences();



     //// Responsible KPIs

    @Query("SELECT COUNT(d) FROM DemandeAbsence d " +
            "WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID AND d.fromDepartment.id_dep = :departmentId")
    Long getTotalValidAbsencesCountByDepartment(@Param("departmentId") Long departmentId
                                                );
    @Query("SELECT new com.example.anywrpfe.dto.TopReasonsForAbsenceDTO(d.absence.typeAbs, COUNT(d), d.fromDepartment.nomDep) " +
            "FROM DemandeAbsence d " +
            "WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID AND d.fromDepartment.id_dep = :departmentId " +
            "GROUP BY d.absence.typeAbs, d.fromDepartment.nomDep " +
            "ORDER BY COUNT(d) DESC")
    List<TopReasonsForAbsenceDTO> findTopReasonsForDepartmentAbsence(@Param("departmentId") Long departmentId
                                                                    );

    @Query("SELECT new com.example.anywrpfe.dto.AbsenceTrendDTO(FUNCTION('MONTH', d.dateDebut), SUM(d.requestedDays)) " +
            "FROM DemandeAbsence d " +
            "WHERE d.statusDemande = com.example.anywrpfe.entities.Enum.Status.VALID AND d.fromDepartment.id_dep = :departmentId " +
            "GROUP BY FUNCTION('MONTH', d.dateDebut) " +
            "ORDER BY FUNCTION('MONTH', d.dateDebut)")
    List<AbsenceTrendDTO> findAbsenceTrends(@Param("departmentId") Long departmentId
                                            );


}
