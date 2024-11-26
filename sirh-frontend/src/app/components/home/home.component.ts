import { Component, OnInit } from '@angular/core';
import {CollaboratorService} from "../../_services/collaborator.service";
import {TokenStorageService} from "../../_services/token-storage.service";
import {Router} from "@angular/router";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  content?: string;

  constructor(private service: CollaboratorService, private tokenStorageService: TokenStorageService,private router: Router,
              private appComponent:AppComponent
  ) {
  }

  ngOnInit(): void {
    const user = this.tokenStorageService.getUser();
    if (user) {
      let roleToCheck = 'ROLE_COLLABORATOR';
      if (user.roles.includes('ROLE_GESTRH')) {
        roleToCheck = 'ROLE_GESTRH';
      } else if (user.roles.includes('ROLE_MANAGER')) {
        roleToCheck = 'ROLE_MANAGER';
      } else if (user.roles.includes('ROLE_ADMIN')) {
        roleToCheck = 'ROLE_ADMIN';
      }
      this.fetchContentBasedOnRole(roleToCheck);
    } else {
      this.content = 'User not logged in.';
    }
    const reloadDone = localStorage.getItem('reloadDone');

    if (!reloadDone) {
      window.location.reload();
    }
    localStorage.setItem('reloadDone', 'true');

  }

  private fetchContentBasedOnRole(role: string): void {
    switch (role) {
      case 'ROLE_GESTRH':
        this.service.getGestRHBoard().subscribe({
          next: data => {
            this.content = data;
          },
          error: err => {
            console.log(err);
            this.content = 'Error fetching GESTRH board content.';
          }
        });
        break;
      case 'ROLE_MANAGER':
        this.service.getManagerBoard().subscribe({
          next: data => {
            this.content = data;
          },
          error: err => {
            console.log(err);
            this.content = 'Error fetching manager board content.';
          }
        });
        break;
      case 'ROLE_ADMIN':
        this.service.getAdminBoard().subscribe({
          next: data => {
            this.content = data;
          },
          error: err => {
            console.log(err);
            this.content = 'Error fetching admin board content.';
          }
        });
        break;
      default:
        this.service.getPublicContent().subscribe({
          next: data => {
            this.content = data;
          },
          error: err => {
            console.log(err);
            this.content = 'Error fetching public content.';
          }
        });
    }
  }

}
