package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.auth.exception.formationExceptions.FormationException;
import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.StatusType;
import com.example.anywrpfe.entities.SpecificationsFilters.DemandeFormationSpecifications;
import com.example.anywrpfe.entities.SpecificationsFilters.FormationSpecifications;
import com.example.anywrpfe.repositories.*;
import com.example.anywrpfe.services.DemandeFormationService;
import com.example.anywrpfe.services.EnrollementService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public  class DemandeFormationServiceImpl implements DemandeFormationService {
    private final CatalogueRepository catalogueRepository;
    private final CompetenceRepository competenceRepository;
    private final FormationRepository formationRepository;
    private final CollaborateurRepository collaborateurRepository;
    private final DemandeFormationRepository demandeFormationRepository;
    private final EnrollementService enrollementService; // Inject EnrollementService

    private static final String ACCEPTED = "ACCEPTED";
    private static final String PENDING = "PENDING";
    private static final String REJECTED = "REJECTED";

    public Page<DemandeFormationDTO> getAllRequests(StatusType status, Long competenceId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Create the base specification
        Specification<DemandeFormation> spec = Specification.where(null);

        // Apply the status filter if it’s provided
        if (status != null) {
            spec = spec.and(DemandeFormationSpecifications.hasStatus(status));
        }

        // Apply the targetCompetence filter if it’s provided
        if (competenceId != null) {
            spec = spec.and(DemandeFormationSpecifications.hasTargetCompetence(competenceId));
        }

        // Retrieve the filtered and paginated results
        Page<DemandeFormation> demandesPage = demandeFormationRepository.findAll(spec, pageable);

        // Map the results to DTOs
        return demandesPage.map(DemandeFormationDTO::fromEntity);
    }




    @Override
    public Page<DemandeFormationDTO> getManagerTrainingRequests(Long managerId ,StatusType status, Long competenceId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Create the base specification
        Specification<DemandeFormation> spec = Specification.where(DemandeFormationSpecifications.hasManagerId(managerId));

        // Apply the status filter if it’s provided
        if (status != null) {
            spec = spec.and(DemandeFormationSpecifications.hasStatus(status));
        }

        // Apply the targetCompetence filter if it’s provided
        if (competenceId != null) {
            spec = spec.and(DemandeFormationSpecifications.hasTargetCompetence(competenceId));
        }

        // Retrieve the filtered and paginated results
        Page<DemandeFormation> demandesPage = demandeFormationRepository.findAll(spec, pageable);

        // Map the results to DTOs
        return demandesPage.map(DemandeFormationDTO::fromEntity);
    }


        @Override
        @Transactional
        public DemandeFormation addRequest(DemandeFormationDTO request) {
            try {
                // Retrieve employee by ID
                Collaborateur employee = collaborateurRepository.findById(request.getEmployee().getId())
                        .orElseThrow(() -> new RuntimeException("Employee with this id is not available: " + request.getEmployee().getId()));

                // Retrieve formation by ID
                Formation formation = formationRepository.findById(request.getFormation().getId())
                        .orElseThrow(() -> new RuntimeException("Formation with this id is not available: " + request.getFormation().getId()));

                // Get the currently authenticated manager's email (assumed to be logged in)
                String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                Collaborateur manager = collaborateurRepository.findByEmail(managerEmail);
                if (manager == null) {
                    throw new FormationException("Manager not found for email: " + managerEmail);
                }

                // Create a new DemandeFormation entity
                DemandeFormation demandeFormation = DemandeFormationDTO.toEntity(request);
                demandeFormation.setEmployee(employee);
                demandeFormation.setFormation(formation);
                demandeFormation.setManager(manager);  // Assign the manager who made the request
                demandeFormation.setStatus(StatusType.PENDING);
                demandeFormation.setCreatedAt(new Date());

                return demandeFormationRepository.save(demandeFormation);
            } catch (Exception e) {
                // Log the exception
                e.printStackTrace(); // For better visibility in logs
                throw new FormationException("Failed to create DemandeFormation: " + e.getMessage());
            }
        }


    @Override
    @Transactional
    public DemandeFormation addCustomizedRequest(DemandeFormationDTO request, FormationDTO data) {



        try {
            // Log the start of the method
            log.info("Starting to process addCustomizedRequest for employee ID: {}", request.getEmployee().getId());

            // Retrieve and validate Competence
            Competence competence = competenceRepository.findById(data.getTargetCompetence().getId())
                    .orElseThrow(() -> {
                        log.error("Competence not found with id: " + data.getTargetCompetence().getId());
                        return new RuntimeException("Competence not found with id: " + data.getTargetCompetence().getId());
                    });
            log.info("Competence found: {}", competence);

            // Create a new Formation
            Formation formation = new Formation();
            formation.setTitle(data.getTitle());
            formation.setDescription(data.getDescription());
            formation.setTargetCompetence(competence);
            formation.setCurrentLevel(data.getCurrentLevel());
            formation.setTargetLevel(data.getTargetLevel());
            formation.setStartingAt(data.getStartingAt());
            formation.setFinishingAt(data.getFinishingAt());

            // Retrieve and validate Catalogue
            Catalogue catalogue = catalogueRepository.findById(data.getCatalogue().getId())
                    .orElseThrow(() -> {
                        log.error("Catalogue not found with id: " + data.getCatalogue().getId());
                        return new RuntimeException("Catalogue not found with id: " + data.getCatalogue().getId());
                    });
            log.info("Catalogue found: {}", catalogue);
            formation.setCatalogue(catalogue);

            // Save the formation
            Formation savedFormation = formationRepository.save(formation);
            log.info("Formation saved successfully with ID: {}", savedFormation.getId());

            // Retrieve and validate the Employee
            Collaborateur employee = collaborateurRepository.findById(request.getEmployee().getId())
                    .orElseThrow(() -> {
                        log.error("Employee with id {} not found", request.getEmployee().getId());
                        return new RuntimeException("Employee with this id is not available");
                    });
            log.info("Employee found: {}", employee);

            // Check for existing pending request
            boolean exists = demandeFormationRepository.existsByEmployeeAndFormation(employee, savedFormation);
            if (exists) {
                log.error("A similar request is already pending for employee: {}", employee.getId());
                throw new FormationException("A similar request is already pending.");
            }

            // Retrieve and validate the Manager
            String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Collaborateur manager = collaborateurRepository.findByEmail(managerEmail);
            log.info("Manager found: {}", manager.getFirstname() + " " + manager.getLastname());

            // Create a new DemandeFormation entity from the DTO
            DemandeFormation newRequest = DemandeFormationDTO.toEntity(request);
            newRequest.setEmployee(employee);  // Set validated employee entity
            newRequest.setFormation(savedFormation);  // Set saved formation
            newRequest.setManager(manager);  // Set the manager
            newRequest.setStatus(StatusType.PENDING);  // Set status to PENDING
            newRequest.setCreatedAt(new Date());  // Set the creation date

            // Save the request to the database
            DemandeFormation savedRequest = demandeFormationRepository.save(newRequest);
            log.info("Request saved successfully with ID: {}", savedRequest.getId());

            return savedRequest;

        } catch (Exception e) {
            log.error("Error while processing addCustomizedRequest: {}", e.getMessage(), e);
            throw new FormationException("Error occurred during request processing: " + e.getMessage());
        }

    }

    @Override
    public void approveRequest(Long id) {
        DemandeFormation request = demandeFormationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DemandeFormation with this ID is not available"));

        if (request.getStatus() != StatusType.PENDING) { // Simplify by checking only for PENDING
            throw new FormationException("This request has already been processed.");
        }

        // Update the status to ACCEPTED
        request.setStatus(StatusType.ACCEPTED);
        demandeFormationRepository.save(request);

        // Handle enrollment addition
        try {
            enrollementService.addEnrollementToCollaborator(id);
        } catch (FormationException e) {
            log.error("Enrollment failed: {}", e.getMessage());
            throw new FormationException("Enrollment failed: " + e.getMessage());
        }
    }




    @Override
    @Transactional
    public void rejectRequest(Long id, String rejectionReason) {

        DemandeFormation request = demandeFormationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DemandeFormation with this id is not available"));

        if (request.getStatus() != StatusType.PENDING) { // Simplify by checking only for PENDING
            throw new FormationException("This request has already been processed.");
        }
        // Update the status to ACCEPTED
        request.setStatus(StatusType.REJECTED);
        demandeFormationRepository.save(request);

    }

    @Override
    public DemandeFormationDTO getById(Long id) {
        // Fetch the DemandeFormation entity from the repository
        Optional<DemandeFormation> demandeFormationOpt = demandeFormationRepository.findById(id);

        // If the entity is found, convert it to a DTO
        if (demandeFormationOpt.isPresent()) {
            DemandeFormation demandeFormation = demandeFormationOpt.get();
            return DemandeFormationDTO.fromEntity(demandeFormation);
        } else {
            // Handle the case where the entity is not found
            throw new EntityNotFoundException("DemandeFormation not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteRequest(Long id) {
        DemandeFormation demandeFormation = demandeFormationRepository.findById(id).orElseThrow(()-> new RuntimeException("Demande formation not found"));

        demandeFormationRepository.delete(demandeFormation);
    }

    @Override
    @Transactional
    public boolean assignCollaboratorToTraining(Long collaboratorId, Long formationId) {
        Optional<Collaborateur> collaboratorOpt = collaborateurRepository.findById(collaboratorId);
        Optional<Formation> formationOpt = formationRepository.findById(formationId);

        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Collaborateur manager = collaborateurRepository.findByEmail(managerEmail);
        if (collaboratorOpt.isPresent() && formationOpt.isPresent()) {
            DemandeFormation demandeFormation = new DemandeFormation();
            demandeFormation.setEmployee(collaboratorOpt.get());
            demandeFormation.setManager(manager);
            demandeFormation.setFormation(formationOpt.get());
            demandeFormation.setStatus(StatusType.PENDING);  // Set initial status as 'PENDING'

            // Save the request
            collaboratorOpt.get().getTrainingRequests().add(demandeFormation);
            manager.getTrainingRequests().add(demandeFormation);

            demandeFormationRepository.save(demandeFormation);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public DemandeFormation createRequestFromSuggestedTraining(Long formationId, Long managerId , Long collaboratorId) {

        // Fetch the formation by its ID
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        // Retrieve and validate the Manager
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Collaborateur manager = collaborateurRepository.findByEmail(managerEmail);
        log.info("Manager found: {}", manager.getFirstname() + " " + manager.getLastname());

        // Fetch the collaborator for whom the training is requested
        Collaborateur targetedCollaborator = collaborateurRepository.findById(collaboratorId)
                .orElseThrow(() -> new RuntimeException("Collaborator not found"));

        // Check if the request for the same training already exists for this collaborator
        boolean alreadyRequested = demandeFormationRepository.existsByFormationIdAndEmployeeId(formationId, targetedCollaborator.getId());
        if (alreadyRequested) {
            throw new FormationException("A request for this training already exists for the collaborator.");
        }

        // Create a new DemandeFormation entity
        DemandeFormation demandeFormation = DemandeFormation.builder()
                .formation(formation)
                .employee(targetedCollaborator)  // The employee for whom the request is made
                .manager(manager)  // The manager making the request
                .status(StatusType.PENDING)  // Initial status
                .createdAt(new Date())  // Request creation date
                .justification("Requested by manager")  // Placeholder justification
                .build();

        // Save the request and return it
         demandeFormationRepository.save(demandeFormation);

        // Optionally return updated DTO or perform other logic
        return demandeFormation;
    }
}
