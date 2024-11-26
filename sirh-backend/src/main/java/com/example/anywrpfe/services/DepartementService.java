package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.DepartementDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Departement;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface DepartementService {

    Departement createDepartement(DepartementDTO departement);
    Optional<DepartementDTO> getDepartementById(Long id);


    Departement updateDepartmentDetails(Long id, DepartementDTO departementDTO);

    void deleteDepartement(Long id);

    @Transactional
    void unassignResponsible(Long departmentId, Long collaboratorId);

    Page<DepartementDTO> getAllDepartementsPage(String name, int page , int size);

    @Transactional
    void setManagerForDepartment(Long collaboratorId, Long departmentId);


    int calculateTotalCollaboratorsInDepartment(Long departmentId);

    Departement findDepartmentByResponsible(Collaborateur responsible);
}
