package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Competence;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompetenceGapDTO {

    private Long competenceId;
    private String competenceName;
    private String posteEvaluation; // Raw string evaluation for the position requirement
    private String collaborateurEvaluation; // Raw string evaluation for the collaborator
    private int gap; // Calculated gap (difference between poste and collaborateur evaluations)
    private int numberOfCollaborators; // Number of collaborators having a gap for this competence

    // Adjusted constructor to initialize with the number of collaborators with a gap
    public CompetenceGapDTO(Long competenceId, String competenceName, String posteEvaluation, String collaborateurEvaluation) {
        this.competenceId = competenceId;
        this.competenceName = competenceName;
        this.posteEvaluation = posteEvaluation;
        this.collaborateurEvaluation = collaborateurEvaluation;
        this.numberOfCollaborators = 0; // Initialize to zero, will be incremented in the service layer as gaps are found
    }

    // Adjusted constructor for aggregating department-wide gap data
    public CompetenceGapDTO(Long competenceId, String competenceName, int gap, int numberOfCollaborators) {
        this.competenceId = competenceId;
        this.competenceName = competenceName;
        this.gap = gap;
        this.numberOfCollaborators = numberOfCollaborators;
    }

    // Method to calculate the gap between the required and collaborator levels
    public void calculateGap(Competence competence) {
        if (this.posteEvaluation != null && this.collaborateurEvaluation != null) {
            int posteEvalNumeric = competence.convertEvaluationToNumeric(this.posteEvaluation, competence.getScaleType());
            int collabEvalNumeric = competence.convertEvaluationToNumeric(this.collaborateurEvaluation, competence.getScaleType());
            this.gap = posteEvalNumeric - collabEvalNumeric;
        } else if (this.posteEvaluation != null) {
            // If the collaborator does not have the competence, the gap is equivalent to the full requirement
            int posteEvalNumeric = competence.convertEvaluationToNumeric(this.posteEvaluation, competence.getScaleType());
            this.gap = posteEvalNumeric;
        } else {
            // If the poste evaluation is not available, set the gap to 0
            this.gap = 0;
        }
    }

    // Method to increment the count of collaborators with a gap
    public void incrementNumberOfCollaborators() {
        this.numberOfCollaborators++;
    }

    // Method to aggregate gap across multiple collaborators
    public void addGap(int additionalGap) {
        this.gap += additionalGap;
    }
}
