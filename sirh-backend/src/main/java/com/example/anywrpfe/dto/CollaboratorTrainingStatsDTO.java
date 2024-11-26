package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Collaborateur;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CollaboratorTrainingStatsDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Integer age;
    private String posteOccupe;

    private Long totalCompletedTrainings;  // New field for training stats

    public static CollaboratorTrainingStatsDTO fromEntityWithStats(Collaborateur collaborateur, Long totalCompletedTrainings) {
        if (collaborateur == null) {
            return null;
        }

        return CollaboratorTrainingStatsDTO.builder()
                .id(collaborateur.getId())
                .firstname(collaborateur.getFirstname())
                .lastname(collaborateur.getLastname())
                .email(collaborateur.getEmail())
                .phone(collaborateur.getPhone())
                .age(collaborateur.getAge())
                .posteOccupe(collaborateur.getPosteOccupe() != null ? collaborateur.getPosteOccupe().getTitre() : null)
                .totalCompletedTrainings(totalCompletedTrainings)  // Set training stats here
                .build();
    }
}
