package com.example.anywrpfe.entities;

import com.example.anywrpfe.entities.Enum.ManagerType;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE collaborateur SET archived = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "archived = false")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class , property = "id")
public class Collaborateur implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    private String password;
    @Column(name = "phone", length = 20, unique = true)
   // @Pattern(regexp = "^\\+216[0-9]{8}$", message = "Phone number must start with +216 and have 8 digits after the indicatif")
    private String phone;
    private Integer age;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private Boolean isEnabled = false;
    private Boolean archived = false;

    @Column(nullable = false)
    private Integer solde = 30;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "collaborateur_roles",
            joinColumns = @JoinColumn(name = "collaborateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(mappedBy = "collaborateur")
    @JsonIgnore
    private List<Token> token;

    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime deletedAt;


    @OneToOne(mappedBy = "responsible")
    @JoinColumn(name="department_id")
    private Departement department;



    @OneToMany(mappedBy = "collaborateur",  cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<CollaborateurCompetence> collaborateurCompetences = new HashSet<>();
    private Boolean isManager;
    private Boolean isGestRH;
    private Boolean isCollab;
    @ManyToOne
    @JsonManagedReference
    private Equipe equipe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Poste posteOccupe;

    private LocalDateTime lastLoginTime;


    @OneToOne(mappedBy = "managerEquipe" , fetch = FetchType.EAGER)
    private Equipe managerEquipe;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DemandeFormation> trainingRequests = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ManagerType managerType;  // NEW: Enum to distinguish between Equipe Manager and Department Responsible

    @Column(nullable = false)
    private Boolean needsPasswordChange = true;

    public Collaborateur(String firstname, String lastname, String email, String password, String phone) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.solde = 30;
    }



    @Override
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;

    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;

    }

    public String  getFullName(){
        return firstname + lastname;
    }
}
