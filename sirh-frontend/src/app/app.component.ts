// src/app/app.component.ts

import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from './_services/token-storage.service';
import { CollaboratorService } from './_services/collaborator.service';
import { Router, NavigationEnd, Event as RouterEvent } from '@angular/router';
import { CatalogueServiceService } from './_services/catalogue-service.service';
import { EquipesService } from './_services/equipes.service';
import { AuthService } from './_services/auth.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'pfeFront';

  catalogues: any[] = [];
  errorMessage: string = '';

  private roles: string[] = [];
  isLoggedIn = false;
  showGestRHBoard = false;
  showManagerBoard = false;
  showCollaboratorBoard = false;
  showAdminBoard = false;
  showCollaboratorsMenu = false;

  ManagerEquipe = false;
  ResponsableDepartment = false;
  isRh = false;
  user: any;
  accueilLink = '/home';

  username?: string;
  showHeaderFooter: boolean = true;

  constructor(
    private tokenStorageService: TokenStorageService,
    private collaboratorService: CollaboratorService,
    private router: Router,
    private catalogueService: CatalogueServiceService,
    private equipeService: EquipesService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    // Monitor navigation events to toggle header/footer display based on route
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.showHeaderFooter = !(event.url === '/login' || event.url === '');

      // Redirect to password change if needed
      if (this.user && this.user.needsPasswordChange && event.url !== '/changePassword') {
        this.router.navigate(['/changePassword']);
      }
    });

    // Subscribe to authentication state changes
    this.authService.isLoggedIn$.subscribe((loggedIn) => {
      this.isLoggedIn = loggedIn;
      if (loggedIn) {
        this.initializeUser();
      } else {
        this.resetUser();
      }
    });

    // Initialize user if already logged in
    if (this.tokenStorageService.isLoggedIn()) {
      this.initializeUser();
    }
  }
  /**
   * Initializes user data and sets up navigation flags based on roles and manager type
   */
  initializeUser(): void {
    this.user = this.tokenStorageService.getUser();
    if (this.user) {
      this.roles = this.user.roles;

      // Set role-based navigation options
      if (this.roles.includes('ROLE_COLLABORATOR') && this.roles.includes('ROLE_RH') && this.roles.includes('ROLE_MANAGER')) {
        this.accueilLink = '/main-dashboard';
        this.showAdminBoard = true;
        this.showCollaboratorsMenu = true;
        this.showGestRHBoard = true;
      } else if (this.roles.includes('ROLE_COLLABORATOR')) {
        this.showCollaboratorBoard = true;
        this.accueilLink = '/CollabDashboard';
      } else if (this.roles.includes('ROLE_MANAGER')) {
        this.showManagerBoard = true;
        this.showCollaboratorsMenu = true;

        this.collaboratorService.findById(this.user.id).subscribe(
          (response) => {
            const userDetails = response;
            if (userDetails) {
              switch (userDetails.managerType) {
                case 'EQUIPE_MANAGER':
                  this.ManagerEquipe = true;
                  this.ResponsableDepartment = false;
                  this.accueilLink = '/ManagerDashboard';
                  break;
                case 'DEPARTMENT_RESPONSIBLE':
                  this.ManagerEquipe = false;
                  this.ResponsableDepartment = true;
                  this.accueilLink = '/responsible-dashboard';
                  break;
                default:
                  this.ManagerEquipe = false;
                  this.ResponsableDepartment = false;
                  this.accueilLink = '/home';
              }
            }
            // Fetch catalogues only after roles have been set and user details loaded
            this.getAllCatalogues();
          },
          (error) => {
            console.error('Error getting user details', error);
          }
        );
      } else {
        this.accueilLink = '/home';
        const rolePriority = ['ROLE_ADMIN', 'ROLE_RH', 'ROLE_MANAGER', 'ROLE_COLLABORATOR'];
        const highestRole = rolePriority.find((role) => this.roles.includes(role));

        switch (highestRole) {
          case 'ROLE_RH':
            this.showGestRHBoard = true;
            this.showCollaboratorsMenu = true;
            this.isRh = true;
            break;
          case 'ROLE_MANAGER':
            this.showManagerBoard = true;
            this.showCollaboratorsMenu = true;
            break;
          case 'ROLE_COLLABORATOR':
            this.showCollaboratorBoard = true;
            break;
        }

        // Fetch catalogues only after roles have been set
        this.getAllCatalogues();
      }

      this.username = this.user.email;
    }
  }
  /**
   * Resets user data and navigation flags upon logout
   */
  resetUser(): void {
    this.user = null;
    this.roles = [];
    this.showAdminBoard = false;
    this.showCollaboratorsMenu = false;
    this.showGestRHBoard = false;
    this.showManagerBoard = false;
    this.showCollaboratorBoard = false;
    this.ManagerEquipe = false;
    this.ResponsableDepartment = false;
    this.isRh = false;
    this.username = undefined;
    this.accueilLink = '/home';
  }

  /**
   * Fetches catalogues for the dropdown menu
   */
  getAllCatalogues(): void {
    this.catalogueService.getAllCatalogues().subscribe(
      (response) => {
        this.catalogues = response.data.catalogues || response.data || [];
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement des catalogues.';
      }
    );
  }

  /**
   * Ensures catalogues are fetched when the user hovers over the Catalogue menu
   */
  onMouseEnterCatalogueDropdown(): void {
    this.getAllCatalogues();
  }

  /**
   * Logs out the user using AuthService
   */
  logout(): void {
    this.authService.logout();
  }

  getAccueilLink(): string {
    return this.accueilLink;
  }
}
