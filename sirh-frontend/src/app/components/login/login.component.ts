// src/app/components/login/login.component.ts

import { Component, OnInit } from '@angular/core';
import { AuthService } from "../../_services/auth.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import {CollaboratorService} from "../../_services/collaborator.service";
import {TokenStorageService} from "../../_services/token-storage.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  isLoginFailed = false;
  errorMessage = '';
  user: any;

  loginForm!: FormGroup;

  ManagerEquipe = false;
  ResponsibleDepartment = false;
  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private collaboratorService:CollaboratorService,
    private tokenStorageService:TokenStorageService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      rememberMe: [false]
    });
  }

  /**
   * Handles the form submission for login
   */
  submitForm(): void {
    if (this.loginForm.invalid) {
      return;
    }

    const { email, password } = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: (data) => {
        console.log('Authentication response: ', data);  // Debugging line to check response
        this.user = data;  // Set user data here

        if (this.user.needsPasswordChange) {
          // Redirect to password change page if needed
          this.router.navigate(['/changePassword'], {
            queryParams: { message: 'Veuillez changer votre mot de passe pour continuer.' }
          });
          return;
        }

        // Existing redirection logic based on roles
        const roles = this.user.roles;
        if (roles.includes('ROLE_RH') || (roles.includes('ROLE_COLLABORATOR') && roles.includes('ROLE_MANAGER'))) {
          this.router.navigate(['/main-dashboard']);
        } else if (roles.includes('ROLE_COLLABORATOR')) {
          this.router.navigate(['/CollabDashboard']);
        } else if (roles.includes('ROLE_MANAGER')) {
          this.collaboratorService.findById(this.user.id).subscribe(
            (response) => {
              const userDetails = response;
              if (userDetails) {
                switch (userDetails.managerType) {
                  case 'EQUIPE_MANAGER':
                    this.router.navigate(['/ManagerDashboard']);
                    break;
                  case 'DEPARTMENT_RESPONSIBLE':
                    this.router.navigate(['/responsible-dashboard']);
                    break;
                  default:
                    this.router.navigate(['/home']);
                }
              }
            },
            (error) => {
              console.error('Error getting user details', error);
            }
          );
        } else {
          this.router.navigate(['/home']);
        }
      },
      error: (err) => {
        if (err.status === 401) {
          this.errorMessage = 'Mot de passe ou email incorrect.';
        } else if (err.status === 400) {
          this.errorMessage = 'Veuillez vérifier vos informations.';
        } else {
          this.errorMessage = 'Mot de passe ou email incorrect, veuillez vérifier vos informations';
        }
        this.isLoginFailed = true;
      }
    });
  }

}
