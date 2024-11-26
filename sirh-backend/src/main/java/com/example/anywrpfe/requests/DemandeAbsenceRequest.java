package com.example.anywrpfe.requests;


import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Enum.Motif;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandeAbsenceRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDebut;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFin;
    private Collaborateur collaborateur;
    private String comment;
    private Motif motif;
}
