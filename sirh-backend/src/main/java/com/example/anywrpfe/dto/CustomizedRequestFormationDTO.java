package com.example.anywrpfe.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomizedRequestFormationDTO {
    private DemandeFormationDTO request;
    private FormationDTO formation;
}
