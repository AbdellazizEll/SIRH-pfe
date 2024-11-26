package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.auth.exception.absenceExceptions.AbsenceExceptions;
import com.example.anywrpfe.config.FileUploadProperties;
import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.ManagerType;
import com.example.anywrpfe.entities.Enum.Motif;
import com.example.anywrpfe.entities.Enum.Status;
import com.example.anywrpfe.repositories.*;
import com.example.anywrpfe.requests.DemandeAbsenceRequest;
import com.example.anywrpfe.services.DemandeAbsenceService;
import com.example.anywrpfe.services.DepartementService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
@Service
public class DemandeAbsenceServiceImpl implements DemandeAbsenceService {


    private final DepartementRepository departementRepository;
    private final CollaborateurRepository collaborateurRepository;

    private final AbsenceRepository absenceRepository;

    private final DemandeAbsenceRepository demandeAbsenceRepository;

    private final FileUploadProperties fileUploadProperties;
    private final DepartementService departementService;


    private static final String ACCEPTED = "ACCEPTED";
    private static final String PENDING = "PENDING";
    private static final String REJECTED = "REJECTED";
    @Override
    // KPI: Absence Rate
    public double calculateAbsenceRate() {
        try {
            int totalWorkingDays= 250;
            int totalAbsenceDays = demandeAbsenceRepository.getTotalAbsenceDays();
            int totalCalculatedWorkingDays = demandeAbsenceRepository.getTotalWorkingDays(totalWorkingDays);

            if (totalCalculatedWorkingDays == 0) return 0;
            double absenceRate = (double) totalAbsenceDays / totalWorkingDays * 100;
            return Math.round(absenceRate * 100.0) / 100.0;

        } catch (Exception e) {
            // Log the exception for debugging
            log.error("Error calculating absence rate: " + e.getMessage());
            e.printStackTrace();  // Optionally print stack trace

            // Return a default value or handle the error appropriately
            return 0;
        }
    }


    @Override
    public List<DepartmentAbsenceRateDTO> calculateAbsenceRateByDepartment() {
        try {
            // Fetch all departments
            List<Departement> allDepartments = departementRepository.findAll();

            List<DepartmentAbsenceRateDTO> departmentAbsenceRates = new ArrayList<>();
            int totalWorkingDays = 250;  // Assume a constant number of working days in the year

            // Iterate over all departments
            for (Departement department : allDepartments) {
                Long departmentId = department.getId_dep();

                // Fetch total absence days and total working days for the department
                int totalAbsenceDays = demandeAbsenceRepository.getTotalAbsenceDaysByDepartment(departmentId);
                int totalCalculatedWorkingDays = demandeAbsenceRepository.getTotalWorkingDaysByDepartment(departmentId, totalWorkingDays);

                double absenceRate = 0.0;
                if (totalCalculatedWorkingDays > 0) {
                    absenceRate = (double) totalAbsenceDays / totalWorkingDays * 100;
                    absenceRate = Math.round(absenceRate * 100.0) / 100.0;
                }

                // Add the result to the DTO list
                departmentAbsenceRates.add(new DepartmentAbsenceRateDTO(departmentId, department.getNomDep(), absenceRate));
            }

            return departmentAbsenceRates;
        } catch (Exception e) {
            // Log the exception for debugging
            log.error("Error calculating absence rate by department: " + e.getMessage());
            e.printStackTrace();  // Optionally print stack trace

            // Return an empty list or handle the error appropriately
            return new ArrayList<>();
        }
    }

    // KPI: Average Absence Duration
    @Override
    public List<AverageAbsenceDurationDTO> calculateAverageAbsenceDuration() {
            return demandeAbsenceRepository.getAverageAbsenceDurationByDepartment();

    }

    @Override
    public List<TopReasonsForAbsenceDTO> findTopReasonsForAbsence() {
        Long totalValidAbsences = demandeAbsenceRepository.getTotalValidAbsencesCount();
        List<TopReasonsForAbsenceDTO> reasonsList = demandeAbsenceRepository.getTopReasonsForAbsenceWithoutPercentage();

        if (totalValidAbsences > 0) {
            reasonsList.forEach(reason -> {
                double percentage = (reason.getAbsenceCount() * 100.0) / totalValidAbsences;
                reason.setAbsencePercentage(percentage);
            });
        }

        return reasonsList;
    }

    @Override
    public List<AbsenteeismByDepartmentDTO> getAbsenteeismByDepartment() {
        return demandeAbsenceRepository.getAbsenteeismByDepartment();
    }

    @Transactional
    @Override
    public DemandeAbsenceDTO AddDemandeAbs(DemandeAbsenceRequest request, MultipartFile justificatif) throws IOException {
        log.info("Starting to add DemandeAbsence");

        // Fetch absence type based on the request motif
        log.info("Received motif: {}", request.getMotif());
        Absence absence = absenceRepository.findByTypeAbs(request.getMotif());
        if (absence == null) {
            throw new AbsenceExceptions("Invalid absence type");
        }

        // Log the absence type enum value
        log.debug("Absence Type Enum: {}", absence.getTypeAbs());

        // Get the authenticated collaborator (user)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collaborateur user = (Collaborateur) authentication.getPrincipal();

        // Determine the origin of the request
        Equipe fromEquipe = null;
        Departement fromDepartment = null;

        // Handle different roles (Equipe Manager or regular collaborator)
        if (user.getManagerType() == ManagerType.EQUIPE_MANAGER) {
            if (user.getManagerEquipe() != null) {
                fromEquipe = user.getManagerEquipe();
                fromDepartment = fromEquipe.getDepartement();
            } else {
                throw new IllegalArgumentException("Equipe Manager is not assigned to any team.");
            }
        } else if (user.getEquipe() != null) {
            fromEquipe = user.getEquipe();
            fromDepartment = fromEquipe.getDepartement();
        } else if (user.getDepartment() != null) {
            fromDepartment = user.getDepartment();
        } else {
            throw new IllegalArgumentException("Collaborator is not assigned to any team or department.");
        }

        // Determine the maximum allowed days based on absence type
        int maxDays = switch (absence.getTypeAbs()) {
            case PAID_LEAVE -> 13;
            case SICK_LEAVE -> 20;
            case UNPAID_LEAVE, MATERNITY_LEAVE, EMERGENCY_LEAVE -> Integer.MAX_VALUE;
            case STUDY_LEAVE -> 2;
            case PARENTAL_LEAVE -> 5;
            case MARRIAGE_LEAVE -> 7;
            default -> throw new IllegalArgumentException("Invalid absence type");
        };

        // Date parsing
        Date startDate = request.getDateDebut();
        Date endDate = request.getDateFin();
        long requestedDays = ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant());

        // Check for overlapping absence requests
        boolean hasOverlappingAbsence = demandeAbsenceRepository.hasOverlappingAbsences(user.getId(), endDate, startDate);
        log.info("Has Overlap: {}", hasOverlappingAbsence);
        if (hasOverlappingAbsence) {
            throw new AbsenceExceptions("Les dates sélectionnées chevauchent une demande existante.");
        }

        // Calculate the total days already taken for this type of absence
        int totalDaysTaken = demandeAbsenceRepository.findByCollaborateurAndAbsence_TypeAbs(user.getId(), absence.getTypeAbs())
                .stream()
                .mapToInt(DemandeAbsence::getRequestedDays)
                .sum();

        if (totalDaysTaken + requestedDays > maxDays) {
            throw new IllegalArgumentException("Requested days exceed the maximum allowed for " + absence.getTypeAbs());
        }

        // File upload is only required for SICK_LEAVE
        String justificatifPath = null;
        if (absence.getTypeAbs() == Motif.SICK_LEAVE && justificatif != null && !justificatif.isEmpty()) {
            log.info("Attempting to save justificatif file: {}", justificatif.getOriginalFilename());
            justificatifPath = saveJustificatif(justificatif);
        } else {
            log.info("No justificatif file provided or file is empty.");
        }

        // Determine approval status based on user's role
        Status managerApproval = Status.PENDING;
        Status responsableApproval = Status.PENDING;

        if (user.getManagerType() == ManagerType.EQUIPE_MANAGER) {
            managerApproval = Status.ACCEPTED_MANAGER;
        } else if (user.getManagerType() == ManagerType.DEPARTMENT_RESPONSIBLE) {
            managerApproval = Status.ACCEPTED_MANAGER;
            responsableApproval = Status.ACCEPTED_RESPONSABLE;
        }

        // Create DemandeAbsence object
        DemandeAbsence demandeAbsence = DemandeAbsence.builder()
                .dateDebut(startDate)
                .datefin(endDate)
                .collaborateur(user)
                .isCreatedAt(new Date())
                .comment(request.getComment())
                .statusDemande(Status.PENDING)
                .approvedByManager(managerApproval)
                .approvedByResponsableDep(responsableApproval)
                .approvedByRh(Status.PENDING)
                .absence(absence)
                .fromDepartment(fromDepartment)
                .fromEquipe(fromEquipe)
                .requestedDays((int) requestedDays)
                .justificatifPath(justificatifPath)
                .build();

        log.info("DemandeAbsence created successfully with justificatifPath: {}", justificatifPath);

        // Save and return the DemandeAbsence
        DemandeAbsence savedDemande = demandeAbsenceRepository.save(demandeAbsence);
        log.info("DemandeAbsence saved with ID: {}", savedDemande.getIdDemandeAb());
        return DemandeAbsenceDTO.fromEntity(savedDemande);
    }




    private String saveJustificatif(MultipartFile justificatif) throws IOException {
        if (justificatif != null && !justificatif.isEmpty()) {
            // Log the upload directory path
            String uploadDir = fileUploadProperties.getUploadDir();
            log.info("Upload directory from properties: {}", uploadDir);

            // Ensure the upload directory exists, if not, create it
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                boolean dirsCreated = uploadDirFile.mkdirs();
                if (!dirsCreated) {
                    log.error("Failed to create directory: {}", uploadDir);
                    throw new IOException("Failed to create directories for file upload");
                }
            }

            // Sanitize and construct the final file path
            String originalFilename = justificatif.getOriginalFilename();
            if (originalFilename == null) {
                throw new IllegalArgumentException("Original filename is null");
            }
            String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            String filePath = uploadDir + sanitizedFilename;

            log.info("Full file path for saving justificatif: {}", filePath);

            File dest = new File(filePath);

            // Save the file to the specified path
            try {
                justificatif.transferTo(dest);
                log.info("File successfully saved to: {}", filePath);
                return "/uploads/justificatifs/" + sanitizedFilename; // Return the relative path for access
            } catch (IOException e) {
                log.error("File saving failed for path: {}", filePath, e);
                throw new IOException("File saving failed", e);
            }
        } else {
            log.warn("No justificatif file provided or file was empty.");
            return null;
        }
    }


    @Override
    public Page<DemandeAbsenceDTO> ListDemandeAbsence( int page, int size) {

        log.info("Fetching AbsenceRequest for page {} of size {}",page,size);

        Page<DemandeAbsence> filteredPage = demandeAbsenceRepository.findAll(PageRequest.of(page, size));

        return filteredPage.map(DemandeAbsenceDTO::fromEntity);
    }
    @Override
    public Page<DemandeAbsenceDTO> ListDemandeAbsEquipeManager(Long managerEquipeId, String name, int page, int size) {
        log.info("Fetching AbsenceRequest for Manager Equipe {},  page {} of size {}",managerEquipeId,page,size);

        Collaborateur managerEquipe = collaborateurRepository.findCollaborateurById(managerEquipeId);

        // Fetch Manager's team
        Equipe equipeManager = managerEquipe.getManagerEquipe();
        log.info("Equipe Manager: {}", equipeManager);
        if (equipeManager == null) {
            throw new IllegalStateException("Manager does not belong to any team.");
                  }

        // Fetch absence requests from the same team
        Page<DemandeAbsence> filteredPage = demandeAbsenceRepository.findByFromEquipeManagerEquipeId(equipeManager.getManagerEquipe().getId(), PageRequest.of(page, size));
        return filteredPage.map(DemandeAbsenceDTO::fromEntity);

    }




    @Override
    public Page<LightDemandeAbsenceDTO> getAbsenceByDepartment(Long departmentId, int page, int size) {
        log.info("Fetching AbsenceRequests for Department {}, page {} of size {}", departmentId, page, size);

        Departement department = departementRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalStateException("Department not found."));

        log.info("Fetching for Department: {} - {}", department.getId_dep(), department.getNomDep());

        // Fetch absence requests from the same department and managers of all equipes in that department
        Page<DemandeAbsence> filteredPage = demandeAbsenceRepository.findByDepartmentWithManagersAndApprovedByManager(department.getId_dep(), PageRequest.of(page, size));
        return filteredPage.map(LightDemandeAbsenceDTO::fromEntity);
    }


    @Override
    public Page<LightDemandeAbsenceDTO> getAbsenceByAuthenticatedResponsibleDepartment(int page, int size) {
        log.info("Fetching AbsenceRequests for authenticated user's department, page {} of size {}", page, size);

        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collaborateur user = (Collaborateur) authentication.getPrincipal();

        // Get the department of the authenticated user
        Departement department = user.getDepartment();
        if (department == null) {
            throw new IllegalStateException("Authenticated user does not belong to any department.");
        }

        log.info("Fetching for Department: {} - {}", department.getId_dep(), department.getNomDep());

        // Fetch absence requests from the same department and managers of all equipes in that department
        Page<DemandeAbsence> filteredPage = demandeAbsenceRepository.findByDepartmentWithManagersAndApprovedByManager(user.getDepartment().getId_dep(), PageRequest.of(page, size));

        log.info("Number of Absence Requests found: {}", filteredPage.getTotalElements());

        return filteredPage.map(LightDemandeAbsenceDTO::fromEntity);
    }



    @Override
    public Page<LightDemandeAbsenceDTO> fetchAbsenceByDepartmentName(String name, int page, int size) {
        log.info("Fetching AbsenceRequests for Department  , page{} of size {} ",page, size);

        Page<DemandeAbsence> filteredPage = demandeAbsenceRepository.findByFromDepartment_NomDepContaining(name, PageRequest.of(page, size));
        return filteredPage.map(LightDemandeAbsenceDTO::fromEntity);
    }




     //ACCEPTED REQUESTS FROM RH



    @Override
    public Page<LightDemandeAbsenceDTO> MesDemandeAbsence(String status,int page, int size) {

        log.info("Fetching AbsenceRequest for my History {},  page {} of size {}",page,size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = ((Collaborateur) authentication.getPrincipal()).getEmail();
        //fetch current User's Absence Rquests:



        Pageable pageable = PageRequest.of(page, size);

        // Handle different statuses based on the input parameter
        if (status == null || status.isEmpty()) {
            // No specific status, return all
            return demandeAbsenceRepository.findByCollaborateurEmail(email, pageable)
                    .map(LightDemandeAbsenceDTO::fromEntity);
        } else if (status.equalsIgnoreCase(PENDING)) {
            return demandeAbsenceRepository
                    .findByCollaborateurEmailAndStatusDemande(
                            email, Status.PENDING, pageable)
                    .map(LightDemandeAbsenceDTO::fromEntity);
        } else if (status.equalsIgnoreCase(ACCEPTED)) {
            return demandeAbsenceRepository
                    .findByCollaborateurEmailAndStatusDemande(
                            email, Status.VALID, pageable)
                    .map(LightDemandeAbsenceDTO::fromEntity);
        } else if (status.equalsIgnoreCase(REJECTED)) {
            return demandeAbsenceRepository
                    .findByCollaborateurEmailAndStatusDemande(
                            email, Status.REJECTED, pageable)
                    .map(LightDemandeAbsenceDTO::fromEntity);
        } else {
            throw new IllegalArgumentException("Invalid status provided");
        }

    }



    @Override
    public DemandeAbsenceDTO rejectDemande(Long demandeId, String refusalReason) {

        //get The collaborator that request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collaborateur user = ((Collaborateur) authentication.getPrincipal());

        boolean isManager = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_MANAGER"));

        boolean isRH = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_RH"));

        DemandeAbsence demandeAbsence = demandeAbsenceRepository.findById(demandeId).orElseThrow();



        if(isManager  || isRH)
        {

            demandeAbsence.setStatusDemande(Status.REJECTED);
            demandeAbsence.setRefusalReason(refusalReason);
            demandeAbsenceRepository.save(demandeAbsence);
            return DemandeAbsenceDTO.fromEntity(demandeAbsence);

        }else{
            throw new IllegalStateException("You are not authorized to reject this absence request.");
        }

    }

    @Override
    @Transactional
    public DemandeAbsenceDTO approveDemandeManagerEquipe(Long demandeId) {

        //get The collaborator that request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collaborateur user = ((Collaborateur) authentication.getPrincipal());

        // Check if the user has the ROLE_DEPARTMENT_RESPONSIBLE role
        boolean isManager = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_MANAGER"));


        DemandeAbsence demandeAbsence = demandeAbsenceRepository.findById(demandeId).orElseThrow();
        if(isManager)
        {
            if(demandeAbsence.getCollaborateur().getId() != user.getId()) {
                demandeAbsence.setStatusDemande(Status.PENDING);
                demandeAbsence.setApprovedByManager(Status.ACCEPTED_MANAGER);

                demandeAbsenceRepository.save(demandeAbsence);
                return DemandeAbsenceDTO.fromEntity(demandeAbsence);

            }
            else {
            throw new IllegalStateException("You are not authorized to approve your absences ");
        }
        }else{
            throw new IllegalStateException("You are not authorized to approve this absence request.");
        }


    }

    @Transactional
    @Override
    public DemandeAbsenceDTO approveByDepartmentResponsible(Long requestId) {

        DemandeAbsence demandeAbsence = demandeAbsenceRepository.findById(requestId).orElseThrow();


            demandeAbsence.setStatusDemande(Status.PENDING);

            demandeAbsence.setApprovedByResponsableDep(Status.ACCEPTED_RESPONSABLE);

        demandeAbsenceRepository.save(demandeAbsence);
        return DemandeAbsenceDTO.fromEntity(demandeAbsence);


    }


    @Transactional
    @Override
    public DemandeAbsenceDTO approveByRH(Long requestId) {


        // Get the collaborator making the request
        DemandeAbsence demandeAbsence = demandeAbsenceRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request ID"));

            demandeAbsence.setStatusDemande(Status.VALID);
            demandeAbsence.setApprovedByRh(Status.ACCEPTED_RH);

            Collaborateur collaborator = demandeAbsence.getCollaborateur();
            int currentSolde = collaborator.getSolde() != null ? collaborator.getSolde() : 0;
            collaborator.setSolde(currentSolde - demandeAbsence.getRequestedDays());
            collaborateurRepository.save(collaborator);
        demandeAbsenceRepository.save(demandeAbsence);
        return DemandeAbsenceDTO.fromEntity(demandeAbsence);
    }





    @Override
    public List<AbsenceDTO> getAllMotifs() {
        return absenceRepository.findAll().stream().map(AbsenceDTO::fromEntity)
                .collect(Collectors.toList());
    }




        //Manager Absences Requests

    @Override
    public Page<LightDemandeAbsenceDTO> getAbsencesManager(
            Long managerId,
            String statusStr,
            String absenceTypeStr,
            Date startDate,
            Date endDate,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // Map status string to enum
        Status status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status provided");
            }
        }

        // Map absence type string to enum
        Motif absenceType = null;
        if (absenceTypeStr != null && !absenceTypeStr.isEmpty()) {
            try {
                absenceType = Motif.valueOf(absenceTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid absence type provided");
            }
        }

        // Fetch absence requests based on filters
        var demandes = demandeAbsenceRepository.findByManagerIdWithFilters(
                managerId, status, absenceType, startDate, endDate, pageable
        );

        return demandes.map(LightDemandeAbsenceDTO::fromEntity);
    }

    @Override
    public Page<LightDemandeAbsenceDTO> getAbsencesRH(
            String statusStr,
            String absenceTypeStr,
            Date startDate,
            Date endDate,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // Map status string to enum
        Status status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status provided");
            }
        }

        // Map absence type string to enum
        Motif absenceType = null;
        if (absenceTypeStr != null && !absenceTypeStr.isEmpty()) {
            try {
                absenceType = Motif.valueOf(absenceTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid absence type provided");
            }
        }

        // Fetch absence requests based on filters
        var demandes = demandeAbsenceRepository.findByRHWithFilters(
                status, absenceType, startDate, endDate, pageable
        );

        return demandes.map(LightDemandeAbsenceDTO::fromEntity);
    }

    @Override
    public Page<LightDemandeAbsenceDTO> getAbsencesResponsible(
            Long responsibleId,
            String statusStr,
            String absenceTypeStr,
            Date startDate,
            Date endDate,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // Map status string to enum
        Status status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status provided");
            }
        }

        // Map absence type string to enum
        Motif absenceType = null;
        if (absenceTypeStr != null && !absenceTypeStr.isEmpty()) {
            try {
                absenceType = Motif.valueOf(absenceTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid absence type provided");
            }
        }

        // Fetch absence requests based on filters
        var demandes = demandeAbsenceRepository.findByResponsibleIdWithFilters(
                responsibleId, status, absenceType, startDate, endDate, pageable
        );

        return demandes.map(LightDemandeAbsenceDTO::fromEntity);
    }

    @Override
    public void removeDemande(Long idDemande)
    {
        log.info("Deleting ID {} : ", idDemande);


        DemandeAbsence demandeAbsence = demandeAbsenceRepository.findById(idDemande).orElseThrow(() -> new RuntimeException("Not Found "));

        demandeAbsenceRepository.delete(demandeAbsence);
    }


    @Override
    public List<CollaboratorAbsenceDTO> findCollaboratorWithMostAbsences() {
        List<Object[]> result = demandeAbsenceRepository.findCollaboratorWithMostAbsences();
        List<CollaboratorAbsenceDTO> absenceDTOs = new ArrayList<>();

        for (Object[] entry : result) {
            // Extract collaborator and absence count
            Collaborateur collaborator = (Collaborateur) entry[0];
            Long absenceCount = (Long) entry[1];

            // Ensure all fields are available to prevent null pointer issues
            if (collaborator != null) {
                absenceDTOs.add(new CollaboratorAbsenceDTO(
                        collaborator.getId(),
                        collaborator.getFirstname() != null ? collaborator.getFirstname() : "N/A",
                        collaborator.getLastname() != null ? collaborator.getLastname() : "N/A",
                        collaborator.getEmail() != null ? collaborator.getEmail() : "N/A",
                        absenceCount != null ? absenceCount : 0L
                ));
            }
        }

        return absenceDTOs;
    }


    @Override
    public List<CollaboratorAbsenceDTO> findCollaboratorWithLeastAbsences() {
        List<Object[]> result = demandeAbsenceRepository.findCollaboratorWithLeastAbsences();
        List<CollaboratorAbsenceDTO> absenceDTOs = new ArrayList<>();

        for (Object[] entry : result) {
            // Extract collaborator and absence count
            Collaborateur collaborator = (Collaborateur) entry[0];
            Long absenceCount = (Long) entry[1];

            // Ensure all fields are available to prevent null pointer issues
            if (collaborator != null) {
                absenceDTOs.add(new CollaboratorAbsenceDTO(
                        collaborator.getId(),
                        collaborator.getFirstname() != null ? collaborator.getFirstname() : "N/A",
                        collaborator.getLastname() != null ? collaborator.getLastname() : "N/A",
                        collaborator.getEmail() != null ? collaborator.getEmail() : "N/A",
                        absenceCount != null ? absenceCount : 0L
                ));
            }
        }

        return absenceDTOs;
    }

    @Override
    public double calculateAbsenteeismRate(Long collaborateurId) {
        List<DemandeAbsence> approvedAbsences = demandeAbsenceRepository.findApprovedAbsencesByCollaborateurId(collaborateurId);
        int totalDaysAbsent = approvedAbsences.stream()
                .mapToInt(DemandeAbsence::calculateRequestedDays)
                .sum();

        int totalWorkingDays = calculateTotalWorkingDays(collaborateurId);
        return totalWorkingDays == 0 ? 0 : (double) totalDaysAbsent / totalWorkingDays * 100;
    }
    private int calculateTotalWorkingDays(Long collaborateurId) {
        // Placeholder implementation, assuming 260 working days in a year
        return 260;
    }
    @Override
    public double calculateAverageAbsenceDuration(Long collaborateurId) {
        List<DemandeAbsence> approvedAbsences = demandeAbsenceRepository.findApprovedAbsencesByCollaborateurId(collaborateurId);
        if (approvedAbsences.isEmpty()) {
            return 0.0;
        }
        int totalDaysAbsent = approvedAbsences.stream()
                .mapToInt(DemandeAbsence::calculateRequestedDays)
                .sum();
        return (double) totalDaysAbsent / approvedAbsences.size();    }


    @Override
    public List<AbsenceReasonDTO> getTopReasonsForAbsence(Long collaborateurId) {
        List<DemandeAbsence> approvedAbsences = demandeAbsenceRepository.findApprovedAbsencesByCollaborateurId(collaborateurId);
        Map<Motif, Long> reasonCount = approvedAbsences.stream()
                .collect(Collectors.groupingBy(abs -> abs.getAbsence().getTypeAbs(), Collectors.counting()));

        return reasonCount.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(entry -> new AbsenceReasonDTO(entry.getKey().toString(), entry.getValue()))
                .limit(3)
                .collect(Collectors.toList());
    }



    //// KPIS for Responsible

    @Override
    public int getTotalAbsenceDaysByDepartment(Long departmentId) {
        return demandeAbsenceRepository.getTotalAbsenceDaysByDepartment(departmentId);
    }


    @Override
    public double calculateAbsenceRateByDepartment(Long departmentId) {
        try {
            int totalAbsenceDays = getTotalAbsenceDaysByDepartment(departmentId);
            int numberOfEmployees = departementService.calculateTotalCollaboratorsInDepartment(departmentId);
            int workingDays = 220; // Annual working days per employee

            if (numberOfEmployees == 0) {
                log.warn("No active collaborators found for department ID: {}", departmentId);
                return 0.0;
            }

            double absenceRate = ((double) totalAbsenceDays) / (numberOfEmployees * workingDays) * 100;
            return Math.round(absenceRate * 100.0) / 100.0;

        } catch (Exception e) {
            log.error("Error calculating absence rate by department {}: {}", departmentId, e.getMessage(), e);
            return 0.0;
        }
    }

    @Override
    public List<TopReasonsForAbsenceDTO> findTopReasonsForDepartmentAbsence(Long departmentId) {
        try {
            List<TopReasonsForAbsenceDTO> topReasons = demandeAbsenceRepository.findTopReasonsForDepartmentAbsence(departmentId);
            Long totalValidAbsences = demandeAbsenceRepository.getTotalValidAbsencesCountByDepartment(departmentId);

            if (totalValidAbsences > 0) {
                for (TopReasonsForAbsenceDTO reason : topReasons) {
                    double percentage = (reason.getAbsenceCount() * 100.0) / totalValidAbsences;
                    reason.setAbsencePercentage(Math.round(percentage * 100.0) / 100.0);
                }
            }

            return topReasons;

        } catch (Exception e) {
            log.error("Error finding top reasons for department absence {}: {}", departmentId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<AbsenceTrendDTO> getAbsenceTrends(Long departmentId) {
        try {
            return demandeAbsenceRepository.findAbsenceTrends(departmentId);
        } catch (Exception e) {
            log.error("Error fetching absence trends for department {}: {}", departmentId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }



}
