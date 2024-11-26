package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.CollaborateurCompetence;
import com.example.anywrpfe.entities.Competence;
import com.example.anywrpfe.entities.Role;
import com.example.anywrpfe.repositories.CollaborateurRepository;
import com.example.anywrpfe.repositories.CompetenceRepository;
import com.example.anywrpfe.repositories.RoleRepository;
import com.example.anywrpfe.services.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final CompetenceRepository competenceRepository;
    private final CollaborateurRepository collaborateurRepository;

    private final RoleRepository roleRepository;

    @Override
    public Role save(Role dto) {
        return roleRepository.save(dto) ;
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(null);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public void delete(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(()-> new RuntimeException("ROLE NOT FOUND"));

        roleRepository.delete(role);
    }

    @Override
    @Transactional
    public void assignRoleToCollaborator(Long collabId, Long idRole) {

        try {

            Collaborateur collaborator = collaborateurRepository.findById(collabId)
                    .orElseThrow(() -> new EntityNotFoundException("Collaborator not found: " + collabId));


            Role role = roleRepository.findById(idRole)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found: " + idRole));


            Set<String> existingRoles = collaborator.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
            if (existingRoles.isEmpty()) {
                log.info("Roles are Empty : ");
                collaborator.getRoles().add(role);
            } else {
                for (String roleSet : existingRoles) {
                    if (roleSet.contains(role.getName())) {
                        log.info("Role exists in this user with role name :" + role.getName());
                    } else {
                        log.info("Inserting a new Role'{}' to this User : {}" + role.getName(), collabId);
                        collaborator.getRoles().add(role);
                    }
                }
            }
        }
        catch (EntityNotFoundException e)
        {
            log.warn("unable " +e );
        }

    }









    @Override
    public void removeRoleFromCollaborator(Long userId, Long roleId) {

        // Find the user by userId
        Collaborateur collaborator = collaborateurRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // Find the role by roleName
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + roleId));

        if (role == null) {
            throw new EntityNotFoundException("Role not found with name: " + role.getName());
        }

        if (!userHasRole(userId, role.getName())) {
            throw new EntityNotFoundException("This UserID : " + userId + " does not have the specified role");
        } else {
            // Remove the role from the collaborator's roles
            collaborator.getRoles().remove(role);

            // Save the updated collaborator
            collaborateurRepository.save(collaborator);

            // Optionally, you can return the updated collaborator DTO here if needed.

        }
    }
        private boolean userHasRole(Long userId, String roleName) {

            Collaborateur user = collaborateurRepository.findById(userId).orElseThrow(null);

            Set<Role> userRoles = user.getRoles();
            for(Role role : userRoles)
            {
                if(role.getName().equals(roleName))
                {
                    return true;
                }
            }
            return false ;
        }

    @Override
    public Page<Collaborateur> getUserRoles(Long userId) {
        return null;
    }

    @Override
    public List<Role> fetchAvailableRole(Long userId) {

        Collaborateur user = collaborateurRepository.findById(userId).orElseThrow(null);

        // Get the roles already assigned to the collaborator
        Set<Role> assignedRoles = user.getRoles();

        // Fetch all roles from the database
        List<Role> allRoles = roleRepository.findAll();

        // Calculate the available roles by filtering out already assigned roles
        return allRoles.stream()
                .filter(role -> !assignedRoles.contains(role))
                .collect(Collectors.toList());



    }

    public List<Competence> fetchAvailableCompetence(Long userId)
    {
        Collaborateur user = collaborateurRepository.findById(userId).orElseThrow(null);

        // GET THE COMPETENCE THAT ALREADY ASSIGNED

        Set<CollaborateurCompetence> assignedCompetence = user.getCollaborateurCompetences().stream()
                .collect(Collectors.toSet());

        // FETCH ALL THE COMPETENCE FROM THE DATABASE

        List<Competence> availableCompetence = competenceRepository.findAll();

        //CALCULATE THE AVAILABLE COMPETENCE BY FILTERING OUT

        return availableCompetence.stream()
            .filter(competence -> !assignedCompetence.contains(competence))
            .collect(Collectors.toList());
    }

    @Override
    public Set<Role> fetchUserAvailableRole(Long userId) {
        Collaborateur user = collaborateurRepository.findById(userId).orElseThrow(null);

        // Get the roles already assigned to the collaborator
         return user.getRoles();




    }



}
