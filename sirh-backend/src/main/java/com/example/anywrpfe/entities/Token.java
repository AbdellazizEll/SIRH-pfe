package com.example.anywrpfe.entities;

import com.example.anywrpfe.entities.Enum.TokenType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(unique = true, length = 512)
    public String token;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    public TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    public boolean expired;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaborateur_id")
    public Collaborateur collaborateur;

    public Token(String token , Collaborateur collaborateur)
    {
        super();
        this.token = token;
        this.collaborateur = collaborateur;
        this.expired = this.isExpired();
    }
}
