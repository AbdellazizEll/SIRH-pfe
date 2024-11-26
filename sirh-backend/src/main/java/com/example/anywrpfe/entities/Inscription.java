package com.example.anywrpfe.entities;


import com.example.anywrpfe.entities.Enum.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The collaborator assigned to this training
    @ManyToOne
    @JoinColumn(name = "collaborator_id", nullable = false)
    private Collaborateur collaborator;

    // The training (Formation) assigned
    @ManyToOne
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;

    // Enrollment and progress fields
    private Date enrollmentDate;        // When the collaborator was enrolled
    private int progress;               // The progress in percentage (0% - 100%)
    private boolean isPendingEvaluation;
    private boolean completed;

    private String evaluationFeedback; // Stores reason for failure
    private Date lastEvaluationDate;  // Whether the training is completed

    @Temporal(TemporalType.DATE)
    private Date completionDate;        // The date when the collaborator finished the training

    private boolean isEnrolled;

    // Method to update progress and mark as completed
    public void updateProgress(int newProgress) {
        this.progress = newProgress;
        if (newProgress >= 100) {
            this.completed = true;
            this.completionDate = new Date(); // Set completion date when finished
        }
    }
}
