package com.example.anywrpfe.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class CollaboratorAbsenceDTO {

    private Long collaboratorId;
    private String firstname;
    private String lastname;
    private String email;
    private Long absenceCount;

    public CollaboratorAbsenceDTO(Long collaboratorId, String firstname, String lastname, String email, Long absenceCount) {
        this.collaboratorId = collaboratorId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.absenceCount = absenceCount;
    }
}
