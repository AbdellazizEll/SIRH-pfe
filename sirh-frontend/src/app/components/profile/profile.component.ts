// src/app/profile/profile.component.ts

import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../../_services/token-storage.service';
import { AuthService } from '../../_services/auth.service';
import { CollaboratorService } from '../../_services/collaborator.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  currentUser: any = {};
  successMessage: string;
  errorMessage: string;

  constructor(
    private authService: AuthService,
    private colllabService: CollaboratorService,
    private storageService: TokenStorageService
  ) {}

  ngOnInit(): void {
    const token = this.storageService.getToken(); // getToken from storage
    if (token) {
      this.authService.getUserFromToken(token).subscribe(
        (user: any) => {
          this.currentUser = user;
        },
        error => {
          console.error("Error fetching user data:", error);
        }
      );
    }
  }

  updateUser(): void {
    console.log("Updating user with the following details:", this.currentUser);

    const payload = {
      firstname: this.currentUser.firstname,
      lastname: this.currentUser.lastname,
      phone: this.currentUser.phone,
      age: this.currentUser.age
    };

    this.colllabService.userUpdate(this.currentUser.id, payload).subscribe(
      next => {
        this.errorMessage = '';
        this.successMessage = 'Les informations de profil ont été mises à jour avec succès';
        setTimeout(() => this.successMessage = '', 3000);

        console.log("Updated user response:", this.currentUser);
      },
      (error) => {
        this.errorMessage = 'Les informations de profil ont échoué';
        this.successMessage = '';
        console.error("Error updating user:", error);

        // Set a timer to clear the messages after 5 seconds
        setTimeout(() => this.successMessage = '', 3000);

      }
    );
  }

  reloadPage(): void {
    window.location.reload();
  }
}
