package com.example.anywrpfe.auth;

import com.example.anywrpfe.entities.Enum.ManagerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;

    @Pattern(regexp = "^\\+216\\d{8}$", message = "Phone number must start with +216 and have 8 digits after the indicatif")
    private String phone;

    private Set<String> role;
    private ManagerType managerType; // EQUIPE_MANAGER, DEPARTMENT_RESPONSIBLE, NONE

    private LocalDateTime createdAt = LocalDateTime.now();
}
