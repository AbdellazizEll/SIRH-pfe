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
    private static final long serialVersionUID = 1L; // Add serialVersionUID for Serializable class

    // Constants for common string literals
    private static final String ONE_STAR = "1 STAR";
    private static final String TWO_STARS = "2 STARS";
    private static final String THREE_STARS = "3 STARS";
    private static final String FOUR_STARS = "4 STARS";

    private static final String FAIBLE = "FAIBLE";
    private static final String MOYEN = "MOYEN";
    private static final String BON = "BON";
    private static final String EXCELLENT = "EXCELLENT";

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
                this.possibleValues = List.of(ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS);
                break;
            case NUMERIC:
                this.possibleValues = List.of("1", "2", "3", "4", "5");
                break;
            case DESCRIPTIF:
                this.possibleValues = List.of(FAIBLE, MOYEN, BON, EXCELLENT);
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
                    case ONE_STAR -> 1;
                    case TWO_STARS -> 2;
                    case THREE_STARS -> 3;
                    case FOUR_STARS -> 4;
                    default -> 0;
                };
            case NUMERIC:
                return Integer.parseInt(evaluation); // Direct numeric evaluation
            case DESCRIPTIF:
                return switch (evaluation) {
                    case FAIBLE -> 1;
                    case MOYEN -> 2;
                    case BON -> 3;
                    case EXCELLENT -> 4;
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
                    case 1 -> ONE_STAR;
                    case 2 -> TWO_STARS;
                    case 3 -> THREE_STARS;
                    case 4 -> FOUR_STARS;
                    default -> "N/A";
                };
            case NUMERIC:
                return String.valueOf(evaluation);
            case DESCRIPTIF:
                return switch (evaluation) {
                    case 1 -> FAIBLE;
                    case 2 -> MOYEN;
                    case 3 -> BON;
                    case 4 -> EXCELLENT;
                    default -> "N/A";
                };
            default:
                throw new IllegalArgumentException("Unknown scale type");
        }
    }
}
