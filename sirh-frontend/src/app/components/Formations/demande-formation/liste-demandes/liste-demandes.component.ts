import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, startWith } from "rxjs";
import { DemandeFormationServiceService } from "../../../../_services/demande-formation-service.service";
import { ApiResponse } from "../../../../_helpers/api-response";
import { Page } from "../../../../_helpers/page";
import { HttpErrorResponse } from "@angular/common/http";
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { RequestRejectionComponent } from "../request-rejection/request-rejection.component";
import { TokenStorageService } from "../../../../_services/token-storage.service";
import { CollaboratorService } from "../../../../_services/collaborator.service";
import { ConfirmDialogComponent } from "../../../Absence/confirm-dialog/confirm-dialog.component";
import { CompetenceService } from "../../../../_services/competence.service";
import {RejectionDialogComponent} from "../../../Absence/rejection-dialog/rejection-dialog.component";

@Component({
  selector: 'app-liste-demandes',
  templateUrl: './liste-demandes.component.html',
  styleUrls: ['./liste-demandes.component.scss']
})
export class ListeDemandesComponent implements OnInit {

  statusMapping: { [key: string]: { displayName: string, style: string } } = {
    'PENDING': { displayName: 'En cours', style: 'badge rounded-pill badge-warning text-dark px-3 py-2' },
    'ACCEPTED': { displayName: 'Validé', style: 'badge rounded-pill badge-success text-light px-3 py-2' },
    'REJECTED': { displayName: 'Refusé', style: 'badge rounded-pill badge-danger text-light px-3 py-2' }
  };

  collaborator: any = null;
  currentTab: 'tous' | 'pending' | 'rejected' | 'accepted' = 'tous';
  currentPage = 0;
  totalPages = 0;

  demandeState$: Observable<{ appState: string, appData?: ApiResponse<Page>, error?: HttpErrorResponse }>;
  responseSubject = new BehaviorSubject<ApiResponse<Page>>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();

  filterStatus: string = ''; // For filtering by status
  filterCompetence: string = ''; // For filtering by target competence
  successMessage: string = '';
  errorMessage: string = '';
  competences: any[] = [];
  isManagerEquipeNull: boolean = false;

  constructor(
    private demandeFormationsService: DemandeFormationServiceService,
    private router: Router,
    private dialog: MatDialog,
    private tokenStorage: TokenStorageService,
    private collaboratorService: CollaboratorService,
    private competenceService: CompetenceService
  ) {}

  ngOnInit(): void {
    const user = this.tokenStorage.getUser();
    this.getCollaboratorInfo(user.id);
    this.loadDemandes(); // Initially load the first page of demandes
    this.loadCompetences();
  }

  getCollaboratorInfo(id: number): void {
    this.collaboratorService.findById(id).subscribe(
      (data) => {
        this.collaborator = data;
        this.isManagerEquipeNull = !this.collaborator?.managerEquipe;
      },
      (error) => {
        console.error('Error fetching collaborator details', error);
      }
    );
  }

  loadCompetences(): void {
    this.competenceService.getAllCompetences().subscribe(
      (response) => {
        console.log('Competences response:', response);
        this.competences = Array.isArray(response.data?.page?.content) ? response.data.page.content : [];
      },
      (error) => {
        console.error('Error loading competences:', error);
      }
    );
  }

  // Load demandes with pagination and filters
  loadDemandes(): void {
    let status = this.filterStatus || this.getStatusFromTab(this.currentTab);
    // Ensure that status is undefined if it's an empty string
    if (!status || status.trim() === '') {
      status = undefined;
    }

    let competenceId: number | undefined;
    if (this.filterCompetence && this.filterCompetence !== '') {
      competenceId = Number(this.filterCompetence);
      if (isNaN(competenceId)) {
        competenceId = undefined;
      }
    } else {
      competenceId = undefined;
    }

    let apiCall: Observable<ApiResponse<Page>>;
    apiCall = this.demandeFormationsService.getAllRequests(status, competenceId, this.currentPageSubject.value);

    this.demandeState$ = apiCall.pipe(
      map((response) => {
        console.log('Demandes response:', response);
        this.responseSubject.next(response);
        this.currentPage = response.data.page.number;
        this.totalPages = response.data.page.totalPages;
        return { appState: 'APP_LOADED', appData: response };
      }),
      startWith({ appState: 'APP_LOADING' }),
      catchError((error) => {
        console.error('Error loading demandes:', error);
        return of({ appState: 'APP_ERROR', error });
      })
    );
  }

  mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      'English': 'Anglais',
      'French': 'Français',
      'JAVA': 'Java',
      'Soft Skills': 'Compétences Douces'
    };
    return competenceMap[name] || name;
  }

  // Apply filters and reset pagination
  filterDemandes(): void {
    this.currentPageSubject.next(0); // Reset to the first page when applying filters
    this.loadDemandes();
  }

  selectTab(tab: 'tous' | 'pending' | 'rejected' | 'accepted'): void {
    if (this.currentTab !== tab) {
      this.currentTab = tab;
      this.filterStatus = ''; // Clear filter status when switching tabs
      this.loadDemandes();
    }
  }

  getStatusFromTab(tab: 'tous' | 'pending' | 'accepted' | 'rejected'): string {
    if (tab === 'tous') return '';
    return tab.toUpperCase();
  }

  getMappedStatus(status: string): string {
    return this.statusMapping[status]?.displayName || status;
  }

  getStatusClass(status: string): string {
    return this.statusMapping[status]?.style || '';
  }

  onStatusChange($event: Event): void {
    this.filterStatus = ($event.target as HTMLSelectElement).value;
    this.loadDemandes();
  }

  goToPage(page: number): void {
    this.currentPageSubject.next(page);
    this.loadDemandes();
  }

  goToNextOrPreviousPage(direction: string): void {
    if (direction === 'forward' && this.currentPage < this.totalPages - 1) {
      this.currentPage++;
    } else if (direction === 'backward' && this.currentPage > 0) {
      this.currentPage--;
    }
    this.goToPage(this.currentPage);
  }


  approveDemande(demandeId: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: "Êtes-vous sûr de vouloir approuver cette demande de formation ?" }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        // Call the service to approve the absence
        this.demandeFormationsService.approveDemande(demandeId).subscribe(
          response => {
            console.log("Accepted", response);
            this.errorMessage = '';
            this.successMessage = "Approbation de la demande a été effectuée.";
            setTimeout(() => this.successMessage = '', 4000);

            // Reload data after approval
            this.loadDemandes();
          },
          error => {
            this.successMessage = '';
            this.errorMessage = "Approbation de la demande a échoué.";
            setTimeout(() => this.errorMessage = '', 4000);
            console.error('Error approving absence', error);
          }
        );
      }
    });
  }
  openRejectModal(demandeId: number): void {
    const dialogRef = this.dialog.open(RejectionDialogComponent, {
      width: '400px', // Set dialog width as needed
      data: { demandeId: demandeId }
    });

    dialogRef.afterClosed().subscribe((rejectionReason: string | null) => {
      if (rejectionReason) {
        this.rejectDemande(demandeId, rejectionReason);
      }
    });
  }

// Method to send the rejection request to the service
  rejectDemande(demandeId: number, reason: string): void {
    this.demandeFormationsService.rejectDemandeTraining(demandeId, reason).subscribe(
      response => {
        this.successMessage = 'La demande a été refusée avec succès.';
        setTimeout(() => this.successMessage = '', 4000);
        this.loadDemandes(); // Reload data to reflect changes
      },
      error => {
        this.errorMessage = 'Le refus de la demande a échoué. Veuillez réessayer.';
        setTimeout(() => this.errorMessage = '', 4000);
      }
    );
  }


  redirectToAjouterDemande() {
    this.router.navigate(["AjoutDemandeFormation"]);
  }


}
