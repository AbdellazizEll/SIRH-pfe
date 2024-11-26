package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Enum.Motif;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopReasonsForAbsenceDTO {
    private Motif typeAbsence;  // The absence type (ENUM)
    private Long absenceCount;  // The count of absences (changed to Long)
    private double absencePercentage; // The percentage of absences of this type compared to the total
    private String departmentName;  // Add a field to specify the department for comparative analysis

    public TopReasonsForAbsenceDTO(Motif typeAbsence, Long absenceCount, String departmentName) {
        this.typeAbsence = typeAbsence;
        this.absenceCount = absenceCount;
        this.departmentName = departmentName;
    }
}
