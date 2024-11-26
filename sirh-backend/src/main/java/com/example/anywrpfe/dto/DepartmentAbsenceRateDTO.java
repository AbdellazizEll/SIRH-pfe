package com.example.anywrpfe.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentAbsenceRateDTO {

    private Long departmentId;
    private String departmentName;
    private double absenceRate;
}
