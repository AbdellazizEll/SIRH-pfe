package com.example.anywrpfe.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@Setter
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String name;
    }





