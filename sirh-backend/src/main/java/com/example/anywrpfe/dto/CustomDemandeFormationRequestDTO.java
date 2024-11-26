package com.example.anywrpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CustomDemandeFormationRequestDTO {

    private DemandeFormationDTO demandeFormationDTO;
    private FormationDTO formationDTO;
}
