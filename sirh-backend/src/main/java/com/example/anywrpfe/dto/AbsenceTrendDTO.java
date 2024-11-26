package com.example.anywrpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbsenceTrendDTO {
    private int month; // 1-12 representing January to December
    private Long totalAbsenceDays;
}
