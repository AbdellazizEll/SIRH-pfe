package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Absence;
import com.example.anywrpfe.entities.Enum.Motif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence,Long> {

    Absence findByTypeAbs(Motif motif);
}
