package com.example.anywrpfe.auth;

import com.example.anywrpfe.config.JwtService;
import com.example.anywrpfe.dto.CollaboratorDTO;
import com.example.anywrpfe.entities.*;
import com.example.anywrpfe.entities.Enum.ManagerType;
import com.example.anywrpfe.entities.Enum.TokenType;
import com.example.anywrpfe.events.RegistrationCompleteEvent;
import com.example.anywrpfe.repositories.AccountVerificationRepository;
import com.example.anywrpfe.repositories.CollaborateurRepository;
import com.example.anywrpfe.repositories.RoleRepository;
import com.example.anywrpfe.repositories.TokenRepository;
import com.example.anywrpfe.services.CollaborateurCompetenceService;
import com.example.anywrpfe.services.implementations.CollaborateurServiceImpl;
import com.example.anywrpfe.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final CollaborateurRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final CollaborateurCompetenceService collaborateurCompetenceService;
    private  final CollaborateurServiceImpl collaborateurService;

    private final AccountVerificationRepository accountVerificationRepository;

    private  final ApplicationEventPublisher publisher;

    private final TokenService tokenService;

    private final RoleRepository roleRepository;


    private static final String ROLE_COLLABORATOR = "ROLE_COLLABORATOR";
    private static final String ROLE_MANAGER = "ROLE_MANAGER";
    private static final String ROLE_RH = "ROLE_RH";

    public AuthenticationResponse register(RegisterRequest request) {
        try {
            log.info("Starting registration process for email: {}", request.getEmail());

            if (repository.getCollaborateurByEmail(request.getEmail()) != 0) {
                log.warn("Email {} is already in use.", request.getEmail());
                return AuthenticationResponse.builder()
                        .message("Error: Email is already in use!")
                        .build();
            }

            // Create new User's account
            Collaborateur collaborateur = new Collaborateur(
                    request.getFirstname(),
                    request.getLastname(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword()),
                    request.getPhone());

            collaborateur.setIsEnabled(false);

            collaborateur.setNeedsPasswordChange(true);
            Set<String> strRoles = request.getRole();
            Set<Role> roles = new HashSet<>();

            // Determine roles and assign managerType if applicable
            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ROLE_COLLABORATOR);
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    log.info("Processing role: {}", role);
                    switch (role) {
                        case "COLLABORATOR":
                            Role collabRole = roleRepository.findByName(ROLE_COLLABORATOR);
                            roles.add(collabRole);
                            collaborateur.setIsCollab(true);
                            collaborateur.setManagerType(ManagerType.NONE); // No manager type for a collaborator
                            break;

                        case "MANAGER":
                            Role managerRole = roleRepository.findByName(ROLE_MANAGER);
                            roles.add(managerRole);
                            collaborateur.setIsManager(true);

                            // Set manager type based on the request (EQUIPE_MANAGER or DEPARTMENT_RESPONSIBLE)
                            if (request.getManagerType() != null) {
                                collaborateur.setManagerType(request.getManagerType());
                            } else {
                                collaborateur.setManagerType(ManagerType.NONE); // Default to NONE if not specified
                            }
                            break;

                        case "RH":
                            Role rhRole = roleRepository.findByName(ROLE_RH);
                            roles.add(rhRole);
                            collaborateur.setIsGestRH(true);
                            collaborateur.setManagerType(ManagerType.NONE); // RH doesn't need a manager type
                            break;

                        default:
                            Role defaultRole = roleRepository.findByName(ROLE_COLLABORATOR);
                            roles.add(defaultRole);
                            collaborateur.setIsCollab(true);
                            collaborateur.setManagerType(ManagerType.NONE);
                            break;
                    }
                });
            }

            collaborateur.setRoles(roles);

            // Save the collaborateur
            repository.save(collaborateur);

            // Publish registration event
            publisher.publishEvent(new RegistrationCompleteEvent(collaborateur, collaborateurService.getVerificationURL(collaborateur.getId().toString())));

            // Initialize competencies after saving collaborateur
            collaborateurCompetenceService.initializeCompetenciesForCollaborator(collaborateur);

            var jwtToken = jwtService.generateToken(collaborateur);
            var refreshToken = jwtService.generateRefreshToken(collaborateur);
            saveUserToken(collaborateur, jwtToken);

            log.info("Registration successful for: {}", request.getEmail());

            if (!collaborateur.isEnabled()) {
                return AuthenticationResponse.builder()
                        .id(collaborateur.getId())
                        .accessToken(jwtToken)
                        .email(collaborateur.getEmail())
                        .message("Please complete the registration by clicking the verification link")
                        .refreshToken(refreshToken)
                        .build();
            }

            return AuthenticationResponse.builder()
                    .id(collaborateur.getId())
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .email(collaborateur.getEmail())
                    .message("Registration Completed, you can log in")
                    .build();

        } catch (Exception e) {
            log.error("Error during registration: ", e);
            return AuthenticationResponse.builder()
                    .message("An unexpected error occurred during registration. Please try again.")
                    .build();
        }
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = (Collaborateur) authentication.getPrincipal();

        if (!user.isEnabled()) {
            return AuthenticationResponse.builder()
                    .message("User is not Verified Yet")
                    .build();
        }

        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        String roleString = roles.stream().collect(Collectors.joining(", ")).replace("[", "").replace("]", "");

        AuthenticationResponse response = AuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .accessToken(jwtToken)
                .message("Collaborator with ID: " + user.getId() + " and ROLE '" + roleString + "' has logged in")
                .roles(roles)
                .build();

        if (user.getNeedsPasswordChange()) {
            response.setNeedsPasswordChange(true);
        }

        return response;
    }

    private void saveUserToken(Collaborateur user, String jwtToken) {
        var token = Token.builder()
                .collaborateur(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Collaborateur user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public String verifyEmail(@RequestParam("token") String token) {
        Optional<Token> thetoken = tokenRepository.findByToken(token);

        if (thetoken.isEmpty()) {
            return "Invalid token";
        }
        Token tokenValue = thetoken.get();
        if (tokenValue.getCollaborateur().isEnabled()) {
            return "This account has already been verified, please log in";
        }

        String verificationResult = tokenService.tokenValidate(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            // Enable the collaborateur here if necessary
            tokenValue.getCollaborateur().setIsEnabled(true);
            repository .save(tokenValue.getCollaborateur());

            return "Email verified Successfully. The user with role "+tokenValue.getCollaborateur().getRoles()+" can login to his account";
        }

        return "INVALID VERIFICATION";
    }



    }

