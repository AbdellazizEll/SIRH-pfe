package com.example.anywrpfe.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Catalogue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @OneToMany(mappedBy = "catalogue", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference // Prevent circular reference during serialization
    private List<Formation> formations;
}
