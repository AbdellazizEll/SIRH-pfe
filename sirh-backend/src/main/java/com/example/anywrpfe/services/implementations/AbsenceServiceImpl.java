package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.entities.Absence;
import com.example.anywrpfe.repositories.AbsenceRepository;
import com.example.anywrpfe.services.AbsenceService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public  class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    @Override
    public Absence saveAbsence(Absence absence) {
        return  absenceRepository.save(absence);
    }

    @Override
    public void deleteAbsence(Absence absence) {
        absenceRepository.delete(absence);
    }
}
