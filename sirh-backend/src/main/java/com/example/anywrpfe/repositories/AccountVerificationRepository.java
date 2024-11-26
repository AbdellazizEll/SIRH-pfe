package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.AccountVerifications;
import com.example.anywrpfe.entities.Collaborateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountVerificationRepository extends JpaRepository<AccountVerifications,Long> {

    AccountVerifications findByCollaborateurId(Long collaborateurId);

   Optional<AccountVerifications> findByCollaborateur(Collaborateur collaborateur);



}
