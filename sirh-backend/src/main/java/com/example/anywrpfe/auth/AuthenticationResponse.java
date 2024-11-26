package com.example.anywrpfe.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String accessToken;
    private Long id;
    private String email;
    private Set<String> roles;
    private String message;

    private String refreshToken;

    private Boolean needsPasswordChange;  // Add this field

    public AuthenticationResponse(String jwtToken, Long id, Set<String> username, String strRoles, String refreshToken) {
    }
}
