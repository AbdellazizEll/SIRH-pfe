package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Events,Long> {

    Events findByType(String type);

}
