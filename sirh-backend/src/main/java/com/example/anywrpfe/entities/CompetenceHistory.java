package com.example.anywrpfe.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CompetenceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "collaborator_id", nullable = false)
    private Collaborateur collaborator;

    @ManyToOne
    @JoinColumn(name = "competence_id", nullable = false)
    private Competence competence;

    private String oldEvaluation;
    private String newEvaluation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;

    private String changeReason;


    // Conversion method
    public int convertEvaluationToNumeric(String evaluation) {
        switch (evaluation) {
            case "1 STAR":
                return 1;
            case "2 STARS":
                return 2;
            case "3 STARS":
                return 3;
            case "4 STARS":
                return 4;
            case "FAIBLE":
                return 1;
            case "MOYEN":
                return 2;
            case "BON":
                return 3;
            case "EXCELLENT":
                return 4;
            // Add more cases based on your scale
            default:
                return 0; // Unknown or undefined evaluation
        }
    }

    public int getNumericNewEvaluation() {
        return convertEvaluationToNumeric(this.newEvaluation);
    }

    public int getNumericOldEvaluation() {
        return convertEvaluationToNumeric(this.oldEvaluation);
    }
}
