package com.example.anywrpfe.entities;

import com.example.anywrpfe.entities.Enum.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DemandeFormation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Collaborateur employee;

    @ManyToOne
    @JoinColumn(name= "manager_id")
    private Collaborateur manager;  // The manager who made the request


    private Date createdAt;

    private String justification;
    @Enumerated(EnumType.STRING)
    private StatusType status = StatusType.PENDING; // e.g., PENDING, APPROVED, REJECTED

    private String rejectionReason; // New field to store the rejection reason




}
