package com.example.anywrpfe.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AccountVerifications implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024) // Update the length to match the database schema
    private String url;

    @ManyToOne(optional = false)
    @JoinColumn(name = "collaborator_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_collaborator_id", value = ConstraintMode.CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (collaborator_id) REFERENCES Collaborateur(id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Collaborateur collaborateur;

}
