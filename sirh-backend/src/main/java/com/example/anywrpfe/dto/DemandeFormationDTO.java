package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.DemandeFormation;
import com.example.anywrpfe.entities.Enum.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class DemandeFormationDTO {


    private Long id;

    private FormationDTO formation;
    private LightCollaboratorDTO employee;
    private LightCollaboratorDTO manager;   // The manager who made the request

    private Date createdAt;

    private String justification;
    private StatusType status = StatusType.PENDING; // e.g., PENDING, APPROVED, REJECTED
    private String rejectionReason; // New field to store the rejection reason



    public static DemandeFormationDTO fromEntity(DemandeFormation demandeFormation) {
        if (demandeFormation == null) {
            return null;
        }

        return DemandeFormationDTO.builder()
                .id(demandeFormation.getId())
                .formation(FormationDTO.fromEntity(demandeFormation.getFormation()))
                .employee(LightCollaboratorDTO.fromEntity(demandeFormation.getEmployee()))
                .manager(LightCollaboratorDTO.fromEntity(demandeFormation.getManager()))
                .createdAt(demandeFormation.getCreatedAt())
                .justification(demandeFormation.getJustification())
                .status(demandeFormation.getStatus())
                .rejectionReason(demandeFormation.getRejectionReason())
                .build();
    }

    public static DemandeFormation toEntity(DemandeFormationDTO dto)
    {
        if(dto == null)
        {
            return null;
        }

        DemandeFormation demandeFormation = new DemandeFormation();
        demandeFormation.setId(dto.getId());
        demandeFormation.setFormation(FormationDTO.toEntity(dto.getFormation()));  // Convert FormationDTO to Formation
        demandeFormation.setEmployee(LightCollaboratorDTO.toEntity(dto.getEmployee()));
        demandeFormation.setManager(LightCollaboratorDTO.toEntity(dto.getManager()));// Convert CollaboratorDTO to Collaborator
        demandeFormation.setJustification(dto.getJustification());
        demandeFormation.setStatus(dto.getStatus());
        demandeFormation.setCreatedAt(dto.getCreatedAt());  // Set createdAt (can be generated automatically too)

        return demandeFormation;
    }
}
