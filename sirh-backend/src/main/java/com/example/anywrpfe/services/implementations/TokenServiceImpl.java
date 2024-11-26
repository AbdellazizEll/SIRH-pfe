package com.example.anywrpfe.services.implementations;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Token;
import com.example.anywrpfe.repositories.CollaborateurRepository;
import com.example.anywrpfe.repositories.TokenRepository;
import com.example.anywrpfe.services.TokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final CollaborateurRepository collaborateurRepository;



    @Override
    public String tokenValidate(String token) {
        Optional<Token> OptionalToken = tokenRepository.findByToken(token);



            Token theToken = OptionalToken.get();
            Collaborateur collaborateur = theToken.getCollaborateur();
            if(theToken.expired)
            {
                tokenRepository.delete(theToken);
                return "Token expired";
            }

            collaborateurRepository.save(collaborateur);

            return "valid";
        }
    }


