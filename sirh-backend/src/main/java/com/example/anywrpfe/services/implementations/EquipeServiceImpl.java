package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.auth.exception.absenceExceptions.AbsenceExceptions;
import com.example.anywrpfe.dto.DepartementDTO;
import com.example.anywrpfe.dto.EquipeDTO;
import com.example.anywrpfe.dto.LightCollaboratorDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Departement;
import com.example.anywrpfe.entities.Enum.ManagerType;
import com.example.anywrpfe.entities.Equipe;
import com.example.anywrpfe.repositories.CollaborateurRepository;
import com.example.anywrpfe.repositories.DepartementRepository;
import com.example.anywrpfe.repositories.EquipeRepository;
import com.example.anywrpfe.services.EquipeService;
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
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Service
public class EquipeServiceImpl implements EquipeService {
    private final DepartementRepository departementRepository;
    private final CollaborateurRepository collaborateurRepository;

    private final EquipeRepository equipeRepository;

    private static final String ROLE_MANAGER = "ROLE_MANAGER";
    private static final String EQUIPE_NOT_FOUND = "Equipe not found with id: ";
    private static final String COLLABORATOR_NOT_FOUND = "Collaborator not found with id: ";
    private static final String MANAGER_NOT_FOUND = "Manager not found with id: ";


    @Override
    @Transactional
    public Equipe createEquipe(EquipeDTO equipeDTO) {
        log.info("Creating Equipe with name: {}", equipeDTO.getNom());

        Equipe equipe = EquipeDTO.toEntity(equipeDTO);

        if (equipeDTO.getFromDepartment() != null) {
            Long depId = equipeDTO.getFromDepartment().getId_dep();
            Departement departement = departementRepository.findById(depId)
                    .orElseThrow(() -> new IllegalArgumentException("Department NOT FOUND"+ depId));
            equipe.setDepartement(departement);
        } else {
            throw new IllegalArgumentException("Department must be specified");
        }

        // Validate and set manager if provided
        if (equipeDTO.getManagerEquipe() != null && equipeDTO.getManagerEquipe().getId() != null) {
            Long managerId = equipeDTO.getManagerEquipe().getId();
            Collaborateur manager = collaborateurRepository.findById(managerId)
                    .orElseThrow(() -> new IllegalArgumentException("Manager not found with id " + managerId));

            boolean isManager = manager.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(ROLE_MANAGER));

            if (!isManager) {
                throw new IllegalArgumentException("Collaborator with id " + managerId + " does not have the authority to be a manager");
            }

            equipe.setManagerEquipe(manager);
            manager.setManagerType(ManagerType.EQUIPE_MANAGER);
        }

        Equipe savedEquipe = equipeRepository.save(equipe);
        log.info("Created Equipe with id: {}", savedEquipe.getId_equipe());
        return savedEquipe;

    }

    @Override
    @Transactional
    public Equipe createStaticEquipeDepartment(EquipeDTO equipeDTO, Long depId) {
        log.info("Creating Equipe with name: {}", equipeDTO.getNom());

        // Fetch the department from the repository
        Departement departement = departementRepository.findById(depId)
                .orElseThrow(() -> {
                    log.error("Department not found with id {}", depId);
                    return new RuntimeException("Department not found with id " + depId);
                });

        // Check if a team with the same name already exists in the department
        if (equipeRepository.existsByNom(equipeDTO.getNom())) {
            log.error("Equipe with name '{}' already exists in department with id {}", equipeDTO.getNom(), depId);
            throw new RuntimeException("A team with the same name already exists in this department");
        }

        // Convert the DTO to an Equipe entity
        Equipe equipeEntity = EquipeDTO.toEntity(equipeDTO);

        // Associate the department with the Equipe entity
        equipeEntity.setDepartement(departement);

        // Save the Equipe entity to the database
        Equipe savedEquipe = equipeRepository.save(equipeEntity);

        log.info("Created Equipe with id: {}", savedEquipe.getId_equipe());

        // Return the saved Equipe entity
        return savedEquipe;
    }

    @Override
    @Transactional
    public Equipe updateEquipe(Long idEquipe, EquipeDTO equipe) {

        // Fetch the team by ID
        Equipe team = equipeRepository.findById(idEquipe)
                .orElseThrow(() -> new EntityNotFoundException(EQUIPE_NOT_FOUND+ idEquipe));

        // Update basic fields if they are provided
        if (equipe.getNom() != null) {
            team.setNom(equipe.getNom());
        }

        if (equipe.getFromDepartment() != null) {
            team.setDepartement(DepartementDTO.toEntity(equipe.getFromDepartment()));
        }

        // Update Manager if Present
        if (equipe.getManagerEquipe() != null) {
            Collaborateur manager = collaborateurRepository.findById(equipe.getManagerEquipe().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found with id " + equipe.getManagerEquipe().getId()));

            if (managerHasRole(manager, ROLE_MANAGER)) {
                team.setManagerEquipe(manager);
            } else {
                throw new IllegalArgumentException("The selected collaborator is not a manager");
            }
        }

        // Update Collaborators if Present
        if (equipe.getCollaborateurs() != null) {
            Set<Collaborateur> collaborators = new HashSet<>();
            for (LightCollaboratorDTO collabDTO : equipe.getCollaborateurs()) {
                Collaborateur collaborator = collaborateurRepository.findById(collabDTO.getId())
                        .orElseThrow(() -> new EntityNotFoundException(COLLABORATOR_NOT_FOUND + collabDTO.getId()));
                collaborators.add(collaborator);
            }
            team.setCollaborateurs(collaborators);
        }

        // Save the updated team
        return equipeRepository.save(team);
    }

    private boolean managerHasRole(Collaborateur manager, String roleName) {
        return manager.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    @Override
    public Page<EquipeDTO> getAllEquipes(String name, int page, int size) {

        log.info("Fetching Departments for page {} of size {}",page,size);
        Page<Equipe> equipes = equipeRepository.findByNomContaining(name, PageRequest.of(page,size));
        return  equipes.map(EquipeDTO::fromEntity);
    }

    @Override
    public Optional<EquipeDTO> findEquipeByManagerId(Long idManager) {
        // Step 1: Fetch the Collaborateur (Manager) entity using the provided ID
        Collaborateur manager = collaborateurRepository.findById(idManager)
                .orElseThrow(() -> new AbsenceExceptions(MANAGER_NOT_FOUND+ idManager));

        // Step 2: Verify if the Collaborateur is actually a Manager of a team
        if (manager.getManagerEquipe() == null) {
            throw new AbsenceExceptions("No team found for the manager with id: " + idManager);
        }

        // Step 3: Fetch the team (Equipe) that the manager manages
        Equipe equipe = manager.getManagerEquipe();

        // Step 4: Convert the Equipe entity to an EquipeDTO
        EquipeDTO equipeDTO = EquipeDTO.fromEntity(equipe);

        // Return the EquipeDTO wrapped in an Optional
        return Optional.of(equipeDTO);
    }

    @Override
    public Optional<EquipeDTO> findEquipeById(Long id) {

        return equipeRepository.findById(id).map(EquipeDTO::fromEntity);
    }



    @Override
    @Transactional
    public void removeCollabFromEquipe(Long id, Long equipeId) {


        Collaborateur removedCollab = collaborateurRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Collab not found with id " + id));

        Equipe equipe = equipeRepository.findById(equipeId).orElseThrow(() -> new EntityNotFoundException(EQUIPE_NOT_FOUND + equipeId));

        equipe.getCollaborateurs().remove(removedCollab);

        equipeRepository.save(equipe);


    }



    @Override
    @Transactional
    public void setCollaboratorToEquipe(Long collaboratorId, Long equipeId) {
        Collaborateur collaborator = collaborateurRepository.findById(collaboratorId)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found with id " + collaboratorId));
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EntityNotFoundException("Equipe not found with id " + equipeId));

        if(collaborator.getEquipe() == null )
        {
            collaborator.setEquipe(equipe);
            collaborateurRepository.save(collaborator);
            equipeRepository.save(equipe);
        }else
        {
            throw new EntityNotFoundException("Collaborator with ID "+collaboratorId);

        }


    }
    @Override
    @Transactional
    public void setListofCollabs(List<Long> collaboratorId, Long equipeId) {
        List<Collaborateur> collaborator = collaborateurRepository.findAllById(collaboratorId);

        if (collaborator.isEmpty()) {
            throw new EntityNotFoundException("No collaborators found with the provided IDs");
        }
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EntityNotFoundException("Equipe not found with id " + equipeId));
        //check of Collaborators without a team
        List<Collaborateur> collaboratorWithoutTeam = collaborator.stream()
                .filter(collaborateur -> collaborateur.getEquipe() == null)
                .collect(Collectors.toList());

        if(collaboratorWithoutTeam.isEmpty()){
            throw  new IllegalArgumentException("All Provided collaborators are already in a team.");
        }
        collaboratorWithoutTeam.forEach(collaborateur -> collaborateur.setEquipe(equipe));
        collaborateurRepository.saveAll(collaboratorWithoutTeam);


    }
    @Override
    @Transactional
    public void setManagerToEquipe(Long collaboratorId, Long equipeId) {


        Collaborateur collaborator = collaborateurRepository.findById(collaboratorId)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found with id " + collaboratorId));
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EntityNotFoundException("equipe not found with id " + equipeId));

        if(collaborator.getIsManager() || collaborator.getRoles().contains(ROLE_MANAGER)) {
            equipe.setManagerEquipe(collaborator);
            collaborateurRepository.save(collaborator);
            equipeRepository.save(equipe);
        }else
        {
            throw  new EntityNotFoundException("Collaborator doesn't have the authority to be Responsible" + collaboratorId);
        }

    }

    @Override
    public Equipe assignManagerToEquipe(Long idManager, Long idEquipe) {
        // Find the team by ID
        Equipe equipe = equipeRepository.findById(idEquipe)
                .orElseThrow(() -> new EntityNotFoundException("Equipe not found with ID: " + idEquipe));

        // Find the manager by ID
        Collaborateur manager = collaborateurRepository.findById(idManager)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with ID: " + idManager));

        // Assign the manager to the team
        equipe.setManagerEquipe(manager);
        manager.setEquipe(equipe);
        collaborateurRepository.save(manager);

        // Save the updated team
        return equipeRepository.save(equipe);
    }


    @Override
    @Transactional
    public void unassignCollaboratorFromEquipe(Long equipeId, Long collaboratorId) {
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EntityNotFoundException("Poste not found"));
        Collaborateur collaborator = collaborateurRepository.findById(collaboratorId)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        collaborator.setEquipe(null); // Unassign the collaborator
        equipe.getCollaborateurs().remove(collaborator);
        equipeRepository.save(equipe);
        collaborateurRepository.save(collaborator);
    }

    @Override
    @Transactional
    public void unassignManagerFromEquipe(Long equipeId, Long collaboratorId) {
        // Fetch and update both the equipe and collaborator objects
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EntityNotFoundException("Equipe not found"));
        Collaborateur collaborator = collaborateurRepository.findById(collaboratorId)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        // Nullify the relationship
        collaborator.setEquipe(null);
        equipe.setManagerEquipe(null);

        // Save both entities to ensure the changes persist
        equipeRepository.save(equipe);
        collaborateurRepository.save(collaborator);
    }
    @Override
    @Transactional
    public void deleteTeam(Long teamId)
    {
        // Fetch the team by ID
        Equipe equipe = equipeRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Equipe not found with ID: " + teamId));

        // Set manager of the team to null (if any)
        if (equipe.getManagerEquipe() != null) {
            equipe.setManagerEquipe(null);
            log.info("Manager set to null for team ID: {}", teamId);
        }

        // Set collaborators' team reference to null
        if (equipe.getCollaborateurs() != null) {
            equipe.getCollaborateurs().forEach(collaborator -> {
                collaborator.setEquipe(null); // Dissociate the team from the collaborator
                collaborateurRepository.save(collaborator); // Save the collaborator
            });
            log.info("Collaborators dissociated from team ID: {}", teamId);
        }

        // Set the department's reference to null (if this team belongs to a department)
        if (equipe.getDepartement() != null) {
            Departement department = equipe.getDepartement();
            equipe.setDepartement(null);  // Dissociate the department from the team
            log.info("Team dissociated from department ID: {}", department.getId_dep());
        }

        // Finally, delete the team
        equipeRepository.delete(equipe);
        log.info("Team with ID: {} deleted successfully", teamId);

    }

    @Override
    @Transactional
    public Equipe assignEquipeToDepartment(Long idEquipe, Long idDep) {
        // Fetch the department by its ID
        Departement department = departementRepository.findById(idDep)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id " + idDep));

        // Fetch the team by its ID
        Equipe equipe = equipeRepository.findById(idEquipe)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id " + idEquipe));

        // Assign the department to the team
        equipe.setDepartement(department);

        // Save the updated team
        return equipeRepository.save(equipe);

    }
}
