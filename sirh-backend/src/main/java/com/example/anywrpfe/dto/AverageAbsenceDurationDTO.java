package com.example.anywrpfe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AverageAbsenceDurationDTO {
    private String departmentName;  // The department name
    private double averageDuration; // Average absence duration in days

    public AverageAbsenceDurationDTO(String departmentName, double averageDuration) {
        this.departmentName = departmentName;
        this.averageDuration = averageDuration;
    }
}
