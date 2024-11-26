package com.example.anywrpfe.dto;

import lombok.Data;

@Data
public class AssignCollaboratorRequest {

    private Long collaboratorId;
    private Long formationId;

    private String justification;
}
