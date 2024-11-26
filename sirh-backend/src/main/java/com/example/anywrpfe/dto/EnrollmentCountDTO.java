package com.example.anywrpfe.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCountDTO {

    private Long departmentId;
    private String departmentName;
    private int enrollmentCount;
}
