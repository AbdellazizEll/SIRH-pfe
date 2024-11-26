package com.example.anywrpfe.services;

import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.requests.DemandeAbsenceRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface DemandeAbsenceService {


    List<AbsenteeismByDepartmentDTO> getAbsenteeismByDepartment();

     DemandeAbsenceDTO AddDemandeAbs(DemandeAbsenceRequest request, MultipartFile justificatif) throws IOException;

    Page<DemandeAbsenceDTO> ListDemandeAbsence( int page , int size);


    Page<DemandeAbsenceDTO> ListDemandeAbsEquipeManager(Long managerEquipeId, String name, int page, int size);



    Page<LightDemandeAbsenceDTO> getAbsenceByDepartment(Long departmentId, int page, int size);



    Page<LightDemandeAbsenceDTO> getAbsenceByAuthenticatedResponsibleDepartment(int page, int size);

    Page<LightDemandeAbsenceDTO> fetchAbsenceByDepartmentName(String name, int page, int size);





    Page<LightDemandeAbsenceDTO> MesDemandeAbsence(String status,int page , int size);




     DemandeAbsenceDTO rejectDemande(Long demandeId, String refusal);



     DemandeAbsenceDTO approveDemandeManagerEquipe(Long demandeId);

    @Transactional
    DemandeAbsenceDTO approveByDepartmentResponsible(Long requestId);


    @Transactional
    DemandeAbsenceDTO approveByRH(Long requestId);

     List<AbsenceDTO> getAllMotifs();


    Page<LightDemandeAbsenceDTO> getAbsencesManager(
            Long managerId,
            String statusStr,
            String absenceTypeStr,
            Date startDate,
            Date endDate,
            int page,
            int size
    ) ;

    Page<LightDemandeAbsenceDTO> getAbsencesRH(
            String statusStr,
            String absenceTypeStr,
            Date startDate,
            Date endDate,
            int page,
            int size
    );

    Page<LightDemandeAbsenceDTO> getAbsencesResponsible(
            Long responsibleId,
            String statusStr,
            String absenceTypeStr,
            Date startDate,
            Date endDate,
            int page,
            int size
    );

    void removeDemande(Long idDemande);

    // KPis


     double calculateAbsenceRate();


    List<DepartmentAbsenceRateDTO> calculateAbsenceRateByDepartment();

    List<AverageAbsenceDurationDTO> calculateAverageAbsenceDuration();

    List<TopReasonsForAbsenceDTO> findTopReasonsForAbsence();


    List<CollaboratorAbsenceDTO> findCollaboratorWithMostAbsences();

     List<CollaboratorAbsenceDTO> findCollaboratorWithLeastAbsences();

    public double calculateAbsenteeismRate(Long collaborateurId);

    public double calculateAverageAbsenceDuration(Long collaborateurId);

    public List<AbsenceReasonDTO> getTopReasonsForAbsence(Long collaborateurId);

    int getTotalAbsenceDaysByDepartment(Long departmentId);
    double calculateAbsenceRateByDepartment(Long departmentId);
    List<TopReasonsForAbsenceDTO> findTopReasonsForDepartmentAbsence(Long departmentId);
    List<AbsenceTrendDTO> getAbsenceTrends(Long departmentId);


    //RESPONSIBLE KPIS



}
