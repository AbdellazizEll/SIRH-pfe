package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.ManagerType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class CollaboratorDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Integer age;
    private Integer solde;
    private Set<String> roles;
    private DepartementDTO responsibleDepartment;
    private LightEquipeDTO managerEquipe;
    private LightEquipeDTO equipeDTO;

    private PosteDTO posteOccupe;
    private List<CollaborateurCompetenceDTO> competenceAcquise;
    private ManagerType managerType;  // NEW field




    public static CollaboratorDTO fromEntity(Collaborateur collaborateur) {
        if (collaborateur == null) {
            return null;
        }

        return CollaboratorDTO.builder()
                .id(collaborateur.getId())
                .firstname(collaborateur.getFirstname())
                .lastname(collaborateur.getLastname())
                .email(collaborateur.getEmail())
                .phone(collaborateur.getPhone())
                .age(collaborateur.getAge())
                .responsibleDepartment(DepartementDTO.fromEntity(collaborateur.getDepartment()))
                .equipeDTO(LightEquipeDTO.fromEntity(collaborateur.getEquipe()))
                .managerEquipe(LightEquipeDTO.fromEntity(collaborateur.getManagerEquipe()))
                .solde(collaborateur.getSolde())
                .roles(collaborateur.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()))
               .competenceAcquise(collaborateur.getCollaborateurCompetences().stream()
                       .map(CollaborateurCompetenceDTO::fromEntity)
                       .collect(Collectors.toList()))
                .posteOccupe(PosteDTO.fromEntity(collaborateur.getPosteOccupe()))
                .managerType(collaborateur.getManagerType()) // NEW mapping

                .build();
    }

    public static Collaborateur toEntity(CollaboratorDTO collaborateurdto) {
        if (collaborateurdto == null) {
            return null;
        }

        Collaborateur collaborateur = new Collaborateur();
        collaborateur.setId(collaborateurdto.getId());
        collaborateur.setFirstname(collaborateurdto.getFirstname());
        collaborateur.setLastname(collaborateurdto.getLastname());
        collaborateur.setEmail(collaborateurdto.getEmail());
        collaborateur.setPhone(collaborateurdto.getPhone());
        collaborateur.setAge(collaborateurdto.getAge());
        collaborateur.setManagerType(collaborateurdto.getManagerType());  // NEW mapping


        return collaborateur;
    }
}
