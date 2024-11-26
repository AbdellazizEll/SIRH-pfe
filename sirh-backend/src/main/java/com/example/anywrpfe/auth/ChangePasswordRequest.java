package com.example.anywrpfe.auth;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required.")
    private String currentPassword;

    @NotBlank(message = "New password is required.")
    @Size(min = 8, message = "New password must be at least 8 characters long.")
    private String newPassword;

    @NotBlank(message = "Password confirmation is required.")
    private String confirmationPassword;
}
