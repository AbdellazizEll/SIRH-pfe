package com.example.anywrpfe.dto;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Enum.ManagerType;
import lombok.Builder;
import lombok.Data;
import org.hibernate.Hibernate;

@Data
@Builder
public class LightCollaboratorDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Integer age;
    private PosteDTO posteOccupe;
    private ManagerType managerType; // NEW field


    public static LightCollaboratorDTO fromEntity(Collaborateur collaborateur) {
        if (collaborateur == null) {
            return null;
        }
        Hibernate.initialize(collaborateur.getPosteOccupe());


        return LightCollaboratorDTO.builder()
                .id(collaborateur.getId())
                .firstname(collaborateur.getFirstname())
                .lastname(collaborateur.getLastname())
                .email(collaborateur.getEmail())
                .phone(collaborateur.getPhone())
                .age(collaborateur.getAge())
                .posteOccupe(PosteDTO.fromEntity(collaborateur.getPosteOccupe()))
                .managerType(collaborateur.getManagerType())  // NEW mapping

                .build();
    }


    public static Collaborateur toEntity(LightCollaboratorDTO dto) {
        if (dto == null) {
            return null;
        }

        Collaborateur collab = new Collaborateur();

        collab.setId(dto.getId());
        collab.setFirstname(dto.getFirstname());
        collab.setLastname(dto.getLastname());
        collab.setEmail(dto.getEmail());
        collab.setAge(dto.getAge());
        collab.setPhone(dto.getPhone());

        return collab;
    }

}
