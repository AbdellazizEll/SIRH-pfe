package com.example.anywrpfe.controller.kpis;

import com.example.anywrpfe.dto.*;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Departement;
import com.example.anywrpfe.services.DemandeAbsenceService;
import com.example.anywrpfe.services.DepartementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/absence-kpis")
@RequiredArgsConstructor
@Slf4j
public class AbsenceKPIController {



    private  final DemandeAbsenceService demandeAbsenceService;
    private final DepartementService departementService;
    // API to get absence rate
    @GetMapping("/absence-rate")
    public ResponseEntity<Double> getAbsenceRate() {
        double absenceRate = demandeAbsenceService.calculateAbsenceRate();
        return ResponseEntity.ok(absenceRate);
    }

    // API to get average absence duration
    @GetMapping("/average-absence-duration")
    public ResponseEntity<List<AverageAbsenceDurationDTO>> getAverageAbsenceDuration() {
        List<AverageAbsenceDurationDTO> avgDuration = demandeAbsenceService.calculateAverageAbsenceDuration();
        return ResponseEntity.ok(avgDuration);
    }

    // API to get absenteeism by department
    @GetMapping("/absenteeism-by-department")
    public ResponseEntity<List<AbsenteeismByDepartmentDTO>> getAbsenteeismByDepartment() {
        List<AbsenteeismByDepartmentDTO> result = demandeAbsenceService.getAbsenteeismByDepartment();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top-reasons")
    public ResponseEntity<List<TopReasonsForAbsenceDTO>> getTopReasonsForAbsence() {
        List<TopReasonsForAbsenceDTO> topReasons = demandeAbsenceService.findTopReasonsForAbsence();
        return ResponseEntity.ok(topReasons);
    }

    @GetMapping("/absence-rate-by-department")
    public ResponseEntity<List<DepartmentAbsenceRateDTO>> getAbsenceRateByDepartment() {
        try {
            List<DepartmentAbsenceRateDTO> absenceRates = demandeAbsenceService.calculateAbsenceRateByDepartment();
            return ResponseEntity.ok(absenceRates);
        } catch (Exception e) {
            log.error("Error calculating absence rate by department: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/absenteeism-rate/{collaborateurId}")
    public ResponseEntity<Double> getAbsenteeismRate(@PathVariable Long collaborateurId) {
        double absenteeismRate = demandeAbsenceService.calculateAbsenteeismRate(collaborateurId);
        return ResponseEntity.ok(absenteeismRate);
    }

    @GetMapping("/average-absence-duration/{collaborateurId}")
    public ResponseEntity<Double> getAverageAbsenceDuration(@PathVariable Long collaborateurId) {
        double averageDuration = demandeAbsenceService.calculateAverageAbsenceDuration(collaborateurId);
        return ResponseEntity.ok(averageDuration);
    }

    @GetMapping("/top-reasons/{collaborateurId}")
    public ResponseEntity<List<AbsenceReasonDTO>> getTopReasonsForAbsence(@PathVariable Long collaborateurId) {
        List<AbsenceReasonDTO> topReasons = demandeAbsenceService.getTopReasonsForAbsence(collaborateurId);
        return ResponseEntity.ok(topReasons);
    }

    @GetMapping("/leave-balance")
    public ResponseEntity<LeaveBalanceDTO> getLeaveBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collaborateur collaborator = (Collaborateur) authentication.getPrincipal();

        LeaveBalanceDTO leaveBalanceDTO = new LeaveBalanceDTO();
        leaveBalanceDTO.setLeaveBalance(collaborator.getSolde());

        return ResponseEntity.ok(leaveBalanceDTO);
    }



    ///RESPONSIBLE KPIs


    private Departement getCurrentResponsibleDepartment() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collaborateur responsible = (Collaborateur) authentication.getPrincipal();
        return departementService.findDepartmentByResponsible(responsible);
    }

    /**
     * Endpoint to get total validated absence days for the Responsible's department.
     */
    @GetMapping("/total-absence-days")
    public ResponseEntity<Integer> getTotalAbsenceDays() {
        try {
            Departement department = getCurrentResponsibleDepartment();
            int totalAbsenceDays = demandeAbsenceService.getTotalAbsenceDaysByDepartment(department.getId_dep());
            return ResponseEntity.ok(totalAbsenceDays);
        } catch (Exception e) {
            log.error("Error fetching total absence days: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to get absence rate (%) for the Responsible's department.
     */
    @GetMapping("/absence-rate-responsible")
    public ResponseEntity<Double> getAbsenceRateResponsible() {
        try {
            Departement department = getCurrentResponsibleDepartment();
            double absenceRate = demandeAbsenceService.calculateAbsenceRateByDepartment(department.getId_dep());
            return ResponseEntity.ok(absenceRate);
        } catch (Exception e) {
            log.error("Error calculating absence rate: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to get absenteeism by department.
     */
    @GetMapping("/absenteeism-by-department-responsible")
    public ResponseEntity<List<AbsenteeismByDepartmentDTO>> getAbsenteeismByDepartmentResponsible() {
        try {
            List<AbsenteeismByDepartmentDTO> absenteeismData = demandeAbsenceService.getAbsenteeismByDepartment();
            return ResponseEntity.ok(absenteeismData);
        } catch (Exception e) {
            log.error("Error fetching absenteeism by department: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Endpoint to get absence trends over time (e.g., monthly) for the Responsible's department.
     */
    @GetMapping("/absence-trends")
    public ResponseEntity<List<AbsenceTrendDTO>> getAbsenceTrends() {
        try {
            Departement department = getCurrentResponsibleDepartment();
            List<AbsenceTrendDTO> trends = demandeAbsenceService.getAbsenceTrends(department.getId_dep());
            return ResponseEntity.ok(trends);
        } catch (Exception e) {
            log.error("Error fetching absence trends: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
