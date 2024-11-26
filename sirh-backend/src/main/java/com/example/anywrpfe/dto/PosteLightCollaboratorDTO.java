package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Collaborateur;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PosteLightCollaboratorDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;


    public static PosteLightCollaboratorDTO fromEntity(Collaborateur collaborateur) {
        if (collaborateur == null) {
            return null;
        }
        return PosteLightCollaboratorDTO.builder()
                .id(collaborateur.getId())
                .firstname(collaborateur.getFirstname())
                .lastname(collaborateur.getLastname())
                .email(collaborateur.getEmail())
                .build();
    }
}
