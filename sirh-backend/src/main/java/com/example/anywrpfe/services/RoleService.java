package com.example.anywrpfe.services;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface RoleService {

    Role save(Role dto);

    Role findById(Long id);

    List<Role> findAll();


    Role findByName(String name);

    void delete(Long id);


    void assignRoleToCollaborator(Long userId, Long idRole);
    void removeRoleFromCollaborator(Long userId, Long roleId);
    Page<Collaborateur> getUserRoles(Long userId);

    List<Role> fetchAvailableRole(Long userId);



    Set<Role> fetchUserAvailableRole(Long userId);



}
