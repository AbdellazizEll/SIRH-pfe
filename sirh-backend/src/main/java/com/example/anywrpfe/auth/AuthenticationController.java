package com.example.anywrpfe.auth;

import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.entities.Token;
import com.example.anywrpfe.repositories.CollaborateurRepository;
import com.example.anywrpfe.repositories.TokenRepository;
import com.example.anywrpfe.services.CollaborateurService;
import com.example.anywrpfe.services.implementations.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;
    private final TokenRepository tokenRepository;
    private final CollaborateurRepository collaborateurRepository;
    private final EmailService emailService;

    private final JavaMailSender mailSender;
    private final CollaborateurService collaborateurService;

    @Value("${activation.url}")
    private String activationUrl;

    @Value("${token.expiration.minutes}")
    private int tokenExpirationMinutes;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    // Authenticate user credentials
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            Collaborateur collaborateur = collaborateurRepository.findByEmail(request.getEmail());

            // Check if the account is activated
            if (!collaborateur.isEnabled()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(AuthenticationResponse.builder()
                                .message("Account not activated. Please check your email for the activation link.")
                                .build());
            }

            // Perform the authentication
            AuthenticationResponse response = service.authenticate(request);

            // Check if the user needs to change their password
            if (collaborateur.getNeedsPasswordChange()) {
                response.setNeedsPasswordChange(true);
                response.setMessage("Password change required. Please change your password.");
            }

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthenticationResponse.builder()
                            .message("Invalid email or password")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthenticationResponse.builder()
                            .message("An unexpected error occurred. Please try again.")
                            .build());
        }
    }


    // Activate account by token
    @GetMapping("/activate-account")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        Optional<Token> activationToken = tokenRepository.findByToken(token);


        if (activationToken.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Expired activation token: {}", token);
            return ResponseEntity.badRequest().body("Activation token has expired");
        }


        Token tokenActivated = activationToken.get();
        Collaborateur collaborateur = activationToken.get().collaborateur;
        collaborateur.setIsEnabled(true);
        collaborateurRepository.save(collaborateur);

        // Optionally remove the token after activation
        tokenRepository.delete(tokenActivated);

        log.info("Account activated for user: {}", collaborateur.getEmail());
        return ResponseEntity.ok("Your account has been successfully activated. You may now log in.");
    }

    // Resend activation email
    @GetMapping("/resend-activation-email")
    public ResponseEntity<String> resendActivationEmail(@RequestParam("email") String email) throws MessagingException {
        Collaborateur collaborateur = collaborateurRepository.findByEmail(email);
        if (collaborateur == null) {
            log.warn("User with email {} not found", email);
            return ResponseEntity.badRequest().body("User with this email not found");
        }

        if (collaborateur.isEnabled()) {
            log.warn("User with email {} is already activated", email);
            return ResponseEntity.badRequest().body("User is already activated");
        }

        // Generate a new activation token
        String newToken = generateAndSaveActivationToken(collaborateur);
        String activationLink = activationUrl + "?token=" + newToken;

        // Send activation email
        emailService.sendEmail(
                collaborateur.getEmail(),
                collaborateur.getFullName(),
                activationLink,
                "Resend Account Activation"
        );

        log.info("Activation email resent to: {}", email);
        return ResponseEntity.ok("Activation email has been resent to " + email);
    }

    // Default endpoint to check API status
    @GetMapping("/")
    public ResponseEntity<String> defaultEndpoint() {
        return ResponseEntity.ok("API is up and running");
    }

    // Test email sending functionality
    @GetMapping("/sendTestMail")
    public String sendMail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("test@example.com");
            message.setSubject("Test Mail");
            message.setText("This is a test mail.");
            mailSender.send(message);
            return "Mail sent successfully!";
        } catch (Exception e) {
            log.error("Failed to send mail: ", e);
            return "Failed to send mail: " + e.getMessage();
        }
    }

    // Helper function to generate and save an activation token
    private String generateAndSaveActivationToken(Collaborateur collaborateur) {
        String generatedToken = UUID.randomUUID().toString();
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(tokenExpirationMinutes))
                .collaborateur(collaborateur)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }



}
