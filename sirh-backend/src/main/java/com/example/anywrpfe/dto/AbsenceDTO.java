package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Absence;
import com.example.anywrpfe.entities.Enum.Motif;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class AbsenceDTO {

    Long absence;
    Motif typeAbs;


    public static  AbsenceDTO fromEntity(Absence absence) {
        if (absence == null) {
            return null;
        }

        return AbsenceDTO.builder()
                .absence(absence.getAbsence())
                .typeAbs(absence.getTypeAbs())
                .build();

    }
    public static Absence toEntity(AbsenceDTO dto) {
        if (dto == null) {
            return null;
        }

        Absence abs = new Absence();
        abs.setAbsence(dto.getAbsence());
        abs.setTypeAbs(dto.getTypeAbs());

        return abs;
    }
}
