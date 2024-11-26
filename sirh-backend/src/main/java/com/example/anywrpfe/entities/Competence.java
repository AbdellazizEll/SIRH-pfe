package com.example.anywrpfe.entities;

import com.example.anywrpfe.entities.Enum.TypeEval;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Competence implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TypeEval scaleType;

    @OneToMany(mappedBy = "competence", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<CollaborateurCompetence> collaborateurCompetences;

    @OneToMany(mappedBy = "competence", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<PosteCompetence> posteCompetences;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> possibleValues = new ArrayList<>();

    public void setScaleType(TypeEval scaleType) {
        this.scaleType = scaleType;
        setDefaultValuesForScaleType(scaleType);
    }

    // Set default possible values for each evaluation type (e.g., STARS, NUMERIC)
    public void setDefaultValuesForScaleType(TypeEval scaleType) {
        switch (scaleType) {
            case STARS:
                this.possibleValues = List.of("1 STAR", "2 STARS", "3 STARS", "4 STARS");
                break;
            case NUMERIC:
                this.possibleValues = List.of("1", "2", "3", "4", "5");
                break;
            case DESCRIPTIF:
                this.possibleValues = List.of("FAIBLE", "MOYEN", "BON", "EXCELLENT");
                break;
            default:
                this.possibleValues = new ArrayList<>();
        }
    }

    // Convert evaluation from string to numeric based on scaleType
    public int convertEvaluationToNumeric(String evaluation, TypeEval scaleType) {
        switch (scaleType) {
            case STARS:
                return switch (evaluation) {
                    case "1 STAR" -> 1;
                    case "2 STARS" -> 2;
                    case "3 STARS" -> 3;
                    case "4 STARS" -> 4;
                    default -> 0;
                };
            case NUMERIC:
                return Integer.parseInt(evaluation);  // Direct numeric evaluation
            case DESCRIPTIF:
                return switch (evaluation) {
                    case "FAIBLE" -> 1;
                    case "MOYEN" -> 2;
                    case "BON" -> 3;
                    case "EXCELLENT" -> 4;
                    default -> 0;
                };
            default:
                throw new IllegalArgumentException("Unknown scale type");
        }
    }

    // Convert numeric evaluation back to string based on scaleType
    public String convertNumericToEvaluation(int evaluation, TypeEval scaleType) {
        switch (scaleType) {
            case STARS:
                return switch (evaluation) {
                    case 1 -> "1 STAR";
                    case 2 -> "2 STARS";
                    case 3 -> "3 STARS";
                    case 4 -> "4 STARS";
                    default -> "N/A";
                };
            case NUMERIC:
                return String.valueOf(evaluation);
            case DESCRIPTIF:
                return switch (evaluation) {
                    case 1 -> "FAIBLE";
                    case 2 -> "MOYEN";
                    case 3 -> "BON";
                    case 4 -> "EXCELLENT";
                    default -> "N/A";
                };
            default:
                throw new IllegalArgumentException("Unknown scale type");
        }
    }
}
