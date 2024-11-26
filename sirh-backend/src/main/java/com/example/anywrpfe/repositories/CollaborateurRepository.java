package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Enum.ManagerType;
import com.example.anywrpfe.entities.Equipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CollaborateurRepository extends JpaRepository<Collaborateur,Long> {

    Collaborateur findByEmail(String email);


    Collaborateur findCollaborateurById(Long id);


    Page<Collaborateur> findAllByFirstnameContaining(String name,Pageable pageable);

    @Query("SELECT c FROM Collaborateur c JOIN c.roles r WHERE r.name = 'ROLE_MANAGER'")
    Page<Collaborateur> findByRoleNameManager(Pageable pageable);




    // Fetch all managers of a specific type
    List<Collaborateur> findByManagerType(ManagerType managerType);
    List<Collaborateur> findByPosteOccupeIsNull();

    List<Collaborateur> findByManagerTypeAndManagerEquipeIsNull(ManagerType managerType);


    List<Collaborateur> findByEquipeNull();
    // Fetch available managers of a specific type (not assigned to any team or department)

    @Query("select count(c) from Collaborateur c where c.email = ?1")
    Integer getCollaborateurByEmail(@Param("email") String email);

    List<Collaborateur> findByEquipe(Equipe equipe);




    @Query("SELECT e.collaborator, COUNT(e) AS totalCompletedTrainings " +
            "FROM Inscription e " +
            "WHERE e.completed = true " +
            "GROUP BY e.collaborator " +
            "ORDER BY totalCompletedTrainings DESC")
    List<Object[]> findCollaboratorWithHighestTrainingCompletion(Pageable pageable);

    // In CollaborateurRepository, add a method to fetch all collaborators belonging to a specific department
    @Query("SELECT coll FROM Collaborateur coll " +
            "LEFT JOIN coll.equipe equipe " +
            "LEFT JOIN equipe.departement dept " +
            "WHERE dept.id_dep = :departmentId " +
            "OR coll.department.id_dep = :departmentId " +
            "OR coll.managerEquipe.departement.id_dep = :departmentId")
    List<Collaborateur> findCollaboratorsByDepartmentId(@Param("departmentId") Long departmentId);


    @Query("SELECT coll FROM Collaborateur coll " +
            "WHERE coll.equipe.id_equipe = :teamId " +
            "OR coll.managerEquipe.id_equipe = :teamId")
    List<Collaborateur> findCollaboratorsByTeamId(@Param("teamId") Long teamId);



    @Query("SELECT COUNT(c) FROM Collaborateur c WHERE c.equipe.id_equipe = :equipeId")
    int getCollaboratorsCountByTeam(@Param("equipeId") Long equipeId);



    @Query("SELECT c FROM Collaborateur c WHERE  c.equipe.departement.id_dep = :departmentId AND c.archived = false")
    List<Collaborateur> findByEquipeDepartementId_dep(@Param("departmentId") Long departmentId);


    @Query("SELECT c FROM Collaborateur c WHERE c.isManager = true AND c.managerEquipe.departement.id_dep = :departmentId AND c.archived = false")
    List<Collaborateur> findManagersByDepartmentId(@Param("departmentId") Long departmentId);


    @Query("SELECT coll FROM Collaborateur coll " +
            "LEFT JOIN FETCH coll.collaborateurCompetences cc " +
            "LEFT JOIN FETCH cc.competence " +
            "LEFT JOIN FETCH coll.posteOccupe po " +
            "LEFT JOIN FETCH po.posteCompetences pc " +
            "WHERE coll.equipe.id_equipe = :teamId " +
            "OR coll.managerEquipe.id_equipe = :teamId")
    List<Collaborateur> findCollaboratorsByTeamIdWithCompetences(@Param("teamId") Long teamId);


}
