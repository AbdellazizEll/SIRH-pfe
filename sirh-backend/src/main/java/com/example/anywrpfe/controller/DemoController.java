package com.example.anywrpfe.controller;

import com.example.anywrpfe.auth.ChangePasswordRequest;
import com.example.anywrpfe.services.CollaborateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequestMapping("/api/v1/demo/")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor

public class DemoController {

    private final CollaborateurService userService;
    @GetMapping("/gestrh")
    @PreAuthorize("hasRole('ROLE_RH')")
    public String gestrhAccess() {
        return "GET:: rh content";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_RH')")
    public String managerAccess() {
        return "GET:: manager and rh content";
    }


    @GetMapping("/collaborator")
    @PreAuthorize("hasRole('ROLE_COLLABORATOR')")
    public String collaboratoraccess() {
        return "GET:: collaborator content";
    }





}
