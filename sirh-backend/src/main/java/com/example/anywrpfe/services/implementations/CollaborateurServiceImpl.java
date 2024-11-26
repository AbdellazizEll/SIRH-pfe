package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.auth.ChangePasswordRequest;
import com.example.anywrpfe.auth.exception.PasswordMismatchException;
import com.example.anywrpfe.auth.exception.WrongPasswordException;
import com.example.anywrpfe.dto.CollaboratorDTO;
import com.example.anywrpfe.dto.CollaboratorTrainingStatsDTO;
import com.example.anywrpfe.dto.LightCollaboratorDTO;
import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.ManagerType;
import com.example.anywrpfe.exception.ApiException;
import com.example.anywrpfe.repositories.*;
import com.example.anywrpfe.services.CollaborateurService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollaborateurServiceImpl implements CollaborateurService {
    private final DepartementRepository departementRepository;

    private final PosteRepository posteRepository;

    private final EquipeRepository equipeRepository;

    private final TokenRepository tokenRepository;

    private final  CollaborateurRepository collaborateurRepository;

    private final PasswordEncoder encoder;

    private final RoleRepository roleRepository;


    @Qualifier("implEmailService")
    private final EmailService emailService;
    @Override
    public Collaborateur create(Collaborateur data) {
        //check the unique email
        Integer emailCount = collaborateurRepository.getCollaborateurByEmail(data.getEmail());
        if(emailCount > 0 ) throw new ApiException("Email already in use. PLease use a different email and try again");
        //save collaborator User
        //saving user with default password
        Collaborateur collab = new Collaborateur();
        collab.setFirstname(data.getFirstname());
        collab.setLastname(data.getLastname());
        collab.setEmail(data.getEmail());
        collab.setPassword(encoder.encode(data.getPassword()));
        collab.setPhone(data.getPhone());
        collab.setAge(data.getAge());
        Set<Role> roles = data.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_COLLABORATOR"));
        }
        collab.setRoles(roles);
        //add Role to the User (depends on the role listed)



        //send email to collaborator with verification URL
        // emailService.sendVerificationURL(collab.getFirstname(), collab.getEmail(), verificationURL, ACCOUNT);
        //return the new created collaborator

        Collaborateur savedCollab = collaborateurRepository.save(collab);

        return savedCollab;

        // if any errors , throw exception with proper message
    }



    public  String getVerificationURL(String token ){
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/auth/verifyEmail")
                .queryParam("token",token)
                .toUriString();

        return url;
    }



    @Override
    public Page<LightCollaboratorDTO> ListCollaborators(String name, int page, int pageSize) {
        log.info("Fetching collaborators for page {} of size {}", page, pageSize);

        Page<Collaborateur> collaboratorsPage = collaborateurRepository.findAllByFirstnameContaining(name, PageRequest.of(page, pageSize));

        return collaboratorsPage.map(LightCollaboratorDTO::fromEntity);
    }
    @Override
    public Optional<CollaboratorDTO> getById(Long id) {
        return collaborateurRepository.findById(id).map(CollaboratorDTO::fromEntity);
    }


    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (Collaborateur) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Check if the current password is correct
        if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new WrongPasswordException("The current password is incorrect.");
        }

        // Check if the new password matches confirmation password
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordMismatchException("The new passwords do not match.");
        }

        // Update the user's password
        Collaborateur collaborateur = collaborateurRepository.findByEmail(user.getEmail());
        collaborateur.setPassword(encoder.encode(request.getNewPassword()));
        collaborateur.setNeedsPasswordChange(false);  // Set to false after changing password
        collaborateurRepository.save(collaborateur);

        // Send confirmation email
        try {
            emailService.sendPasswordChangeConfirmationEmail(collaborateur.getEmail(), collaborateur.getFirstname() + " " + collaborateur.getLastname());
        } catch (MessagingException e) {
            log.error("Failed to send password change confirmation email.", e);
        }
    }

    @Override
    public Collaborateur laodUserByUserName(String email) {
        return collaborateurRepository.findByEmail(email);
    }

    @Override
    public LightCollaboratorDTO updateCollab(Long id, LightCollaboratorDTO collab) {

        Collaborateur user = collaborateurRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));

            user.setFirstname(collab.getFirstname());
            user.setLastname(collab.getLastname());
            user.setPhone(collab.getPhone());
            user.setAge(collab.getAge());
        Collaborateur updatedCollab = collaborateurRepository.save(user);

        return LightCollaboratorDTO.fromEntity(updatedCollab);

    }

    @Override
    public Collaborateur updateLoggedUser(CollaboratorDTO collaborateur) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collaborateur userDetails = (Collaborateur) authentication.getPrincipal();

        if (userDetails.isEnabled()) {
            Collaborateur _collab = userDetails;
            _collab.setFirstname(collaborateur.getFirstname());
            _collab.setLastname(collaborateur.getLastname());
            _collab.setPhone(collaborateur.getPhone());
            _collab.setAge(collaborateur.getAge());
            Collaborateur updatedCollab = collaborateurRepository.save(_collab);

            return updatedCollab;
        } else {
            return null;
        }
    }

    @Override
    public List<CollaboratorDTO> findEquipeManagers() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findByManagerType(ManagerType.EQUIPE_MANAGER);
        return collaborateurs.stream()
                .map(CollaboratorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LightCollaboratorDTO> findEquipeManagersAvailable() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findByManagerTypeAndManagerEquipeIsNull(ManagerType.EQUIPE_MANAGER);
        return collaborateurs.stream()
                .map(LightCollaboratorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void unAssignManagerFromEquipe(Long equipeId, Long collaboratorId) {
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EntityNotFoundException("Poste not found"));
        Collaborateur collaborator = collaborateurRepository.findById(collaboratorId)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        collaborator.setEquipe(null); // Unassign the collaborator
        equipe.setManagerEquipe(null);
        equipeRepository.save(equipe);
        collaborateurRepository.save(collaborator);
    }
    @Override
    public List<CollaboratorDTO> findDepartmentResponsibles() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findByManagerType(ManagerType.DEPARTMENT_RESPONSIBLE);
        return collaborateurs.stream()
                .map(CollaboratorDTO::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional
    @Override
    public void unAssignDepartmentResponsible(Long departmentId, Long collaboratorId) {
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
    public List<CollaboratorDTO> findResponsibleWithoutDepartments() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findByManagerType(ManagerType.DEPARTMENT_RESPONSIBLE);
        return collaborateurs.stream()
                .map(CollaboratorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LightCollaboratorDTO> findJoblessCollaborators() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findByPosteOccupeIsNull();

        return collaborateurs.stream()
                .map(LightCollaboratorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LightCollaboratorDTO> findTeamlessCollaborators() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findByEquipeNull();

        return collaborateurs.stream()
                .map(LightCollaboratorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void updateManagerType(Long collaborateurId, ManagerType newType) {
        Optional<Collaborateur> collaborateurOpt = collaborateurRepository.findById(collaborateurId);
        if (collaborateurOpt.isPresent()) {
            Collaborateur collaborateur = collaborateurOpt.get();
            collaborateur.setManagerType(newType);
            collaborateurRepository.save(collaborateur);
        }
    }

    @Override
    public Boolean remove(Long id) {
        return collaborateurRepository.findById(id).map(collab -> {
            collab.setArchived(true);
            collab.setDeletedAt(LocalDateTime.now());
            collaborateurRepository.save(collab);
            return true;
        }).orElse(false);
    }

//    @Override
//    @Transactional
//    public void assignCollaboratorToDepartment(Long collaboratorId, Long departmentId) {
//        Collaborateur collaborator = collaborateurRepository.findById(collaboratorId)
//                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found with id " + collaboratorId));
//        Departement department = departementRepository.findById(departmentId)
//                .orElseThrow(() -> new EntityNotFoundException("Department not found with id " + departmentId));
//
//        collaborator.setDepartment(department);
//        collaborateurRepository.save(collaborator);
//    }


    @Override
    public Page<LightCollaboratorDTO>ListCollaboratorsManager(int page, int size) {
        log.info("Fetching collaborators  who has Role Manager , for page {} of size {}",page,size);

        Page<Collaborateur> collaboratorsPage = collaborateurRepository.findByRoleNameManager(PageRequest.of(page, size));

        return collaboratorsPage.map(LightCollaboratorDTO::fromEntity);
    }


    @Override
    public Page<CollaboratorDTO>FetchResponsiblesAvailable(int page, int size) {

        List<Collaborateur> allCollaborators = collaborateurRepository.findAll();

        List<Collaborateur> filteredCollaborators = allCollaborators.stream()
                .filter(collaborateur -> collaborateur.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_MANAGER")) && // Check if they have the "ROLE_MANAGER" role
                        collaborateur.getManagerEquipe() == null && // Ensure they are not managing any team
                        collaborateur.getDepartment() == null) // Ensure they are not assigned to any department
                .collect(Collectors.toList());

        int start = Math.min((int) PageRequest.of(page, size).getOffset(), filteredCollaborators.size());
        int end = Math.min((start + size), filteredCollaborators.size());

        Page<Collaborateur> collaboratorsPage = new PageImpl<>(filteredCollaborators.subList(start, end), PageRequest.of(page, size), filteredCollaborators.size());

        return collaboratorsPage.map(CollaboratorDTO::fromEntity);
    }



    @Override
    public Page<CollaboratorDTO>ListCollaboratorsManagerWithOutEquipe(int page, int size) {

        List<Collaborateur> allCollaborators = collaborateurRepository.findAll();

        List<Collaborateur> filteredCollaborators = allCollaborators.stream()
                .filter(collaborateur -> collaborateur.getRoles().size() == 1 &&
                        collaborateur.getRoles().iterator().next().getName().equals("ROLE_MANAGER") &&
                        collaborateur.getManagerEquipe() == null && collaborateur.getDepartment()  == null)
                .collect(Collectors.toList());

        int start = Math.min((int) PageRequest.of(page, size).getOffset(), filteredCollaborators.size());
        int end = Math.min((start + size), filteredCollaborators.size());

        Page<Collaborateur> collaboratorsPage = new PageImpl<>(filteredCollaborators.subList(start, end), PageRequest.of(page, size), filteredCollaborators.size());

        return collaboratorsPage.map(CollaboratorDTO::fromEntity);
    }


    @Override
    public Page<LightCollaboratorDTO> ListCollaboratorsCollaborator(int page, int size) {

        List<Collaborateur> allCollaborators = collaborateurRepository.findAll();

        List<Collaborateur> filteredCollaborators = allCollaborators.stream()
                .filter(collaborateur -> collaborateur.getRoles().size() == 1 &&
                        collaborateur.getRoles().iterator().next().getName().equals("ROLE_COLLABORATOR") &&
                        collaborateur.getManagerEquipe() == null && collaborateur.getDepartment()  == null && collaborateur.getEquipe() == null)
                .collect(Collectors.toList());

        int start = Math.min((int) PageRequest.of(page, size).getOffset(), filteredCollaborators.size());
        int end = Math.min((start + size), filteredCollaborators.size());

        Page<Collaborateur> collaboratorsPage = new PageImpl<>(filteredCollaborators.subList(start, end), PageRequest.of(page, size), filteredCollaborators.size());

        return collaboratorsPage.map(LightCollaboratorDTO::fromEntity);
    }

    @Override
    public Page<LightCollaboratorDTO> FetchCollaborators(int page, int size) {

        List<Collaborateur> allCollaborators = collaborateurRepository.findAll();

        List<Collaborateur> filteredCollaborators = allCollaborators.stream()
                .filter(collaborateur -> collaborateur.getRoles().size() == 1 &&
                        collaborateur.getRoles().iterator().next().getName().equals("ROLE_COLLABORATOR"))
                .collect(Collectors.toList());

        int start = Math.min((int) PageRequest.of(page, size).getOffset(), filteredCollaborators.size());
        int end = Math.min((start + size), filteredCollaborators.size());

        Page<Collaborateur> collaboratorsPage = new PageImpl<>(filteredCollaborators.subList(start, end), PageRequest.of(page, size), filteredCollaborators.size());

        return collaboratorsPage.map(LightCollaboratorDTO::fromEntity);
    }

    @Override
    @Transactional
    public CollaboratorDTO assignPosteToCollaborateur(Long collaborateurId, Long posteId) {
        Collaborateur collaborateur = collaborateurRepository.findById(collaborateurId)
                .orElseThrow(() -> new RuntimeException("Collaborateur not found"));
        Poste poste = posteRepository.findById(posteId)
                .orElseThrow(() -> new RuntimeException("Poste not found"));

        if(collaborateur.getPosteOccupe() != null ) {
            new EntityNotFoundException("Cannot Add post to this collaborator");
        }

            collaborateur.setPosteOccupe(poste);
             collaborateurRepository.save(collaborateur);

             CollaboratorDTO dto= CollaboratorDTO.fromEntity(collaborateur);
             return dto;

    }

    @Override
    @Transactional
    public CollaboratorDTO removePosteFromCollaborateur(Long collaborateurId) {
        Collaborateur collaborateur = collaborateurRepository.findById(collaborateurId)
                .orElseThrow(() -> new RuntimeException("Collaborateur not found"));

        collaborateur.setPosteOccupe(null);
         collaborateurRepository.save(collaborateur);
         CollaboratorDTO dto = CollaboratorDTO.fromEntity(collaborateur);
         return dto ;
    }

    @Override
    public List<LightCollaboratorDTO> findCollaboratorsByTeam(Long idEquipe) {
        // Fetch the team by ID, throwing an exception if the team is not found
        Equipe equipe = equipeRepository.findById(idEquipe)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + idEquipe));

        // Fetch the collaborators that are part of the given team
        List<Collaborateur> collaborateurs = collaborateurRepository.findByEquipe(equipe);

        // Convert the list of Collaborateur entities to CollaboratorDTOs and return the list
        return collaborateurs.stream()
                .map(LightCollaboratorDTO::fromEntity)
                .collect(Collectors.toList());
    }



    /// KPI



//    @Override
//    public Collaborateur getCollaboratorWithMostTrainingProgress() {
//        List<Object[]> result = enrollementRepository.findCollaboratorWithMostTrainingProgress(PageRequest.of(0, 1));
//        return (Collaborateur) result.get(0)[0]; // Get the collaborator with the highest progress
//    }
@Override
public CollaboratorTrainingStatsDTO getCollaboratorWithHighestTrainingCompletion() {
    List<Object[]> result = collaborateurRepository.findCollaboratorWithHighestTrainingCompletion(PageRequest.of(0, 1));

    // Validate result
    if (result == null || result.isEmpty()) {
        throw new RuntimeException("No collaborator found with training completions.");
    }

    Object[] resultData = result.get(0);

    // Ensure proper data format
    if (resultData.length < 2 || resultData[0] == null || resultData[1] == null) {
        throw new RuntimeException("Invalid result format from findCollaboratorWithHighestTrainingCompletion.");
    }

    // Extract collaborator and training completion count
    Collaborateur collaborator = (Collaborateur) resultData[0];
    Long totalCompletedTrainings = (Long) resultData[1];

    // Ensure collaborator and training stats are valid
    return CollaboratorTrainingStatsDTO.fromEntityWithStats(collaborator, totalCompletedTrainings);
}


}
