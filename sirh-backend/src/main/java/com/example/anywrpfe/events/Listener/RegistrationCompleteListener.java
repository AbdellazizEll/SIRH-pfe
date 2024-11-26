package com.example.anywrpfe.events.Listener;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Token;
import com.example.anywrpfe.events.RegistrationCompleteEvent;
import com.example.anywrpfe.repositories.AccountVerificationRepository;
import com.example.anywrpfe.repositories.TokenRepository;
import com.example.anywrpfe.services.implementations.CollaborateurServiceImpl;
import com.example.anywrpfe.services.implementations.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final TokenRepository tokenRepository;
    private final CollaborateurServiceImpl collaborateurService;

    @Qualifier("implEmailService")
    private final EmailService emailService;
    private final AccountVerificationRepository accountVerificationRepository;

    @Value("${activation.url}")
    private String activationUrl;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        Collaborateur collaborateur = event.getCollaborateur();

        try {
            log.info("Generating activation token for collaborateur: {}", collaborateur.getEmail());
            String activationToken = generateAndSaveActivationToken(collaborateur);

            String activationLink = activationUrl + "?token=" + activationToken;
            log.info("Activation link generated: {}", activationLink);

            emailService.sendEmail(
                    collaborateur.getEmail(),
                    collaborateur.getFullName(),
                    activationLink,
                    "Account Activation"
            );

            log.info("Activation email sent successfully to {}", collaborateur.getEmail());

        } catch (MessagingException e) {
            log.error("Failed to send activation email for {}", collaborateur.getEmail(), e);
            throw new RuntimeException("Failed to send activation email", e);
        } catch (Exception e) {
            log.error("Unexpected error during event processing for {}", collaborateur.getEmail(), e);
            throw new RuntimeException("Unexpected error during event processing", e);
        }
    }

    private String generateAndSaveActivationToken(Collaborateur collaborateur) {
        String generatedToken = UUID.randomUUID().toString();
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .collaborateur(collaborateur)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }
}
