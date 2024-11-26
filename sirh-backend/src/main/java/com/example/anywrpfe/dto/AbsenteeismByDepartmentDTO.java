package com.example.anywrpfe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AbsenteeismByDepartmentDTO {

    private String departmentName;   // The name of the department
    private Long totalAbsenceDays;   // The total number of absence days (changed to Long)

    public AbsenteeismByDepartmentDTO(String departmentName, Long totalAbsenceDays) {
        this.departmentName = departmentName;
        this.totalAbsenceDays = totalAbsenceDays;
    }
}
