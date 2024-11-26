package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.DemandeAbsence;
import com.example.anywrpfe.entities.Enum.Status;
import lombok.Builder;
import lombok.Data;
import org.hibernate.Hibernate;

import java.util.Date;

@Builder
@Data
public class LightDemandeAbsenceDTO {

    private Long id;
    private Date dateDebut;

    private Date datefin;


    private String justificatifPath;


    private Status statusDemande;

    @Builder.Default
    private Status approvedByManager = Status.PENDING;
    @Builder.Default
    private Status approvedByResponsableDep = Status.PENDING;
    @Builder.Default
    private Status approvedByRh = Status.PENDING;


    private String comment;
    private AbsenceDTO absence;
    private LightCollaboratorDTO collaborateur;


    public static LightDemandeAbsenceDTO fromEntity(DemandeAbsence demandeAbsence) {
        if (demandeAbsence == null) {
            return null;
        }

        // Initialize lazy-loaded fields
        Hibernate.initialize(demandeAbsence.getCollaborateur().getPosteOccupe());
        Hibernate.initialize(demandeAbsence.getFromEquipe().getCollaborateurs());
        Hibernate.initialize(demandeAbsence.getFromEquipe().getManagerEquipe());
        Hibernate.initialize(demandeAbsence.getFromDepartment().getEquipeList());


        return LightDemandeAbsenceDTO.builder()
                .id(demandeAbsence.getIdDemandeAb())
                .dateDebut(demandeAbsence.getDateDebut())
                .datefin(demandeAbsence.getDatefin())
                .comment(demandeAbsence.getComment())
                .statusDemande(demandeAbsence.getStatusDemande())
                .justificatifPath(demandeAbsence.getJustificatifPath())
                .approvedByManager(demandeAbsence.getApprovedByManager())
                .approvedByResponsableDep(demandeAbsence.getApprovedByResponsableDep())
                .approvedByRh(demandeAbsence.getApprovedByRh())
                .collaborateur(LightCollaboratorDTO.fromEntity(demandeAbsence.getCollaborateur()))
                .absence(AbsenceDTO.fromEntity(demandeAbsence.getAbsence()))
                .build();

    }


    public static DemandeAbsence toEntity(DemandeAbsenceDTO dto) {
        if (dto == null) {
            return null;
        }

        DemandeAbsence demandeAbsence = new DemandeAbsence();
        demandeAbsence.setIdDemandeAb(dto.getId());
        demandeAbsence.setAbsence(AbsenceDTO.toEntity(dto.getAbsence()));
        demandeAbsence.setDateDebut(dto.getDateDebut());
        demandeAbsence.setDatefin(dto.getDatefin());
        demandeAbsence.setComment(dto.getComment());
        demandeAbsence.setStatusDemande(dto.getStatusDemande());
        demandeAbsence.setApprovedByManager(dto.getApprovedByManager());
        demandeAbsence.setApprovedByResponsableDep(dto.getApprovedByResponsableDep());
        demandeAbsence.setApprovedByRh(dto.getApprovedByRh());
        demandeAbsence.setCollaborateur(LightCollaboratorDTO.toEntity(dto.getCollaborateur()));
        demandeAbsence.setFromDepartment(DepartementDTO.toEntity(dto.getFromDepartment()));

        return demandeAbsence;
    }

}
