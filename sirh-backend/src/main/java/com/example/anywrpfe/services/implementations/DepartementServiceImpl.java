
package com.example.anywrpfe.services.implementations;
import com.example.anywrpfe.dto.DepartementDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Departement;
import com.example.anywrpfe.entities.Equipe;
import com.example.anywrpfe.repositories.CollaborateurRepository;
import com.example.anywrpfe.repositories.DepartementRepository;
import com.example.anywrpfe.repositories.EquipeRepository;
import com.example.anywrpfe.services.DepartementService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartementServiceImpl implements DepartementService {
    private final EquipeRepository equipeRepository;
    private final CollaborateurRepository collaborateurRepository;

    private final DepartementRepository departementRepository;

    @Override
    public Departement createDepartement(DepartementDTO dto) {
        // Convert DTO to entity
        Departement departement = DepartementDTO.toEntity(dto);

        // Save the department
        Departement savedDepartement = departementRepository.save(departement);

        // Check if a responsible is provided
        if (dto.getResponsable() != null) {
            Collaborateur responsable = collaborateurRepository.findById(dto.getResponsable().getId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));

            // Assign the manager to the department
            savedDepartement.setResponsible(responsable);
            savedDepartement = departementRepository.save(savedDepartement);
        }

        return savedDepartement;

    }

    @Override
    public Optional<DepartementDTO> getDepartementById(Long id) {
        return departementRepository.findById(id).map(DepartementDTO::fromEntity);
    }

    @Override
    public Departement updateDepartmentDetails(Long id, DepartementDTO departementDTO) {
        return departementRepository.findById(id).map(existingDepartment -> {
            if (departementDTO.getNomDep() != null) {
                existingDepartment.setNomDep(departementDTO.getNomDep());
            }
            // Do not handle 'responsable' here
            return departementRepository.save(existingDepartment);
        }).orElseThrow(() -> new IllegalArgumentException("Department not found"));
    }

    @Override
    public void deleteDepartement(Long id) {
        Departement departement =departementRepository.findById(id).orElseThrow(() -> new RuntimeException("Department is NULL"));
    departementRepository.delete(departement);
    }


    @Override
    @Transactional
    public void setManagerForDepartment(Long departmentId, Long collaboratorId) {

        Collaborateur collaborator = collaborateurRepository.findById(collaboratorId)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found with id " + collaboratorId));
        Departement department = departementRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id " + departmentId));
        boolean isManager = collaborator.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_MANAGER"));

        if (isManager || collaborator.getDepartment() == null && department.getResponsible() == null   ) {
            department.setResponsible(collaborator);
            departementRepository.save(department);
        } else {
            throw new EntityNotFoundException("Collaborator doesn't have the authority to be Responsible with id " + collaboratorId);
        }

    }

    @Override
    @Transactional
    public void unassignResponsible(Long departmentId, Long collaboratorId) {
        Departement departement = departementRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Poste not found"));
        Collaborateur collaborator = collaborateurRepository.findById(collaboratorId)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        collaborator.setDepartment(null); // Unassign the collaborator
        departement.setResponsible(null);
        departementRepository.save(departement);
        collaborateurRepository.save(collaborator);
    }


    @Override
    public Page<DepartementDTO> getAllDepartementsPage(String name,int page, int size) {
        log.info("Fetching Departments for page {} of size {}",page,size);
    Page<Departement> departements = departementRepository.findByNomDepContaining(name,PageRequest.of(page,size));
        return  departements.map(DepartementDTO::fromEntity);
    }



    @Override
    public int calculateTotalCollaboratorsInDepartment(Long departmentId) {
        Set<Long> collaboratorIds = new HashSet<>();

        // 1. Fetch all active team members in the department
        List<Collaborateur> teamMembers = collaborateurRepository.findByEquipeDepartementId_dep(departmentId);
        for (Collaborateur member : teamMembers) {
            collaboratorIds.add(member.getId());
        }

        // 2. Fetch all active team managers in the department
        List<Collaborateur> teamManagers = collaborateurRepository.findManagersByDepartmentId(departmentId);
        for (Collaborateur manager : teamManagers) {
            collaboratorIds.add(manager.getId());
        }

        // 3. Fetch the department responsible
        Optional<Departement> departmentOpt = departementRepository.findById(departmentId);
        if (departmentOpt.isPresent()) {
            Collaborateur responsible = departmentOpt.get().getResponsible();
            if (responsible != null && !responsible.getArchived()) {
                collaboratorIds.add(responsible.getId());
            }
        }

        return collaboratorIds.size();
    }

    @Override
    public Departement findDepartmentByResponsible(Collaborateur responsible) {
        Optional<Departement> departmentOpt = departementRepository.findByResponsible(responsible);

        return departmentOpt.orElseThrow(() -> new RuntimeException("Department not found for the responsible user."));
    }

}
