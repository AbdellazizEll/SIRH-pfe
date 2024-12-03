package com.example.anywrpfe.repositories;

import com.example.anywrpfe.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String roleName);

}
