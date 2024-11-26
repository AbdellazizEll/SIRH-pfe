package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Formation;
import com.example.anywrpfe.entities.Inscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedTrainingDTO {
    private Long trainingId;
    private String trainingName;
    private String description;
    private int progress; // Progress in percentage

    public static AssignedTrainingDTO fromEntity(Inscription inscription) {
        Formation training = inscription.getFormation();
        return new AssignedTrainingDTO(
                training.getId(),
                training.getTitle(),
                training.getDescription(),
                inscription.getProgress()  // Get current progress of the enrollment
        );
    }
}
