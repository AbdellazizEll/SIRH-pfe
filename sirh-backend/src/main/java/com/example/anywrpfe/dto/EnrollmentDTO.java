package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Inscription;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class EnrollmentDTO {

    private Long id;

    private LightCollaboratorDTO collaboratorDTO;

    private FormationDTO formationDTO;

    private int progress;

    private boolean completed;

    private Date enrollmentDate;

    private Date completionDate;

    private boolean isEnrolled;



    public static EnrollmentDTO fromEntity(Inscription inscription) {
        if (inscription == null) {
            return null;
        }

        return EnrollmentDTO.builder()
                .id(inscription.getId())
                .formationDTO(FormationDTO.fromEntity(inscription.getFormation()))
                .collaboratorDTO(LightCollaboratorDTO.fromEntity(inscription.getCollaborator()))
                .progress(inscription.getProgress())
                .completed(inscription.isCompleted())
                .enrollmentDate(inscription.getEnrollmentDate())
                .completionDate(inscription.getCompletionDate())
                .isEnrolled(inscription.isEnrolled())
                .build();
    }

    public static Inscription toEntity(EnrollmentDTO dto)
    {
        if(dto == null)
        {
            return null;
        }

        Inscription inscription = new Inscription();
        inscription.setId(dto.getId());
        inscription.setFormation(FormationDTO.toEntity(dto.getFormationDTO()));  // Convert FormationDTO to Formation
        inscription.setCollaborator(LightCollaboratorDTO.toEntity(dto.getCollaboratorDTO()));
        inscription.setProgress(dto.getProgress());// Convert CollaboratorDTO to Collaborator
        inscription.setCompleted(dto.isCompleted());
        inscription.setEnrollmentDate(dto.getEnrollmentDate());
        inscription.setCompletionDate(dto.getCompletionDate());
        inscription.setEnrolled(dto.isEnrolled);

        return inscription;
    }
}
