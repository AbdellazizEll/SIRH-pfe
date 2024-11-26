package com.example.anywrpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingOverviewDTO {
    private long completedTrainings;
    private long inProgressTrainings;
}
