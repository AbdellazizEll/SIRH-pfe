import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, catchError, of, startWith, map } from 'rxjs';
import { DemandeFormationServiceService } from "../../../../_services/demande-formation-service.service";
import { ApiResponse } from "../../../../_helpers/api-response";
import { Page } from "../../../../_helpers/page";
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { CatalogueServiceService } from "../../../../_services/catalogue-service.service";
import {PositionDetailModalComponent} from "../../../Poste/position-detail-modal/position-detail-modal.component";
import {MatDialog} from "@angular/material/dialog";
import {DemandeModifierComponent} from "../demande-modifier/demande-modifier.component";
import {TokenStorageService} from "../../../../_services/token-storage.service";
import {Router} from "@angular/router";
import {CompetenceService} from "../../../../_services/competence.service";

@Component({
  selector: 'app-historique',
  templateUrl: './historique.component.html',
  styleUrls: ['./historique.component.scss']
})
export class HistoriqueComponent implements OnInit {

  statusMapping: { [key: string]: { displayName: string, style: string } } = {
    'PENDING': { displayName: 'En cours', style: 'badge rounded-pill badge-warning text-dark px-3 py-2' },
    'ACCEPTED': { displayName: 'Validé', style: 'badge rounded-pill badge-success text-light px-3 py-2' },
    'REJECTED': { displayName: 'Refusé', style: 'badge rounded-pill badge-danger text-light px-3 py-2' }
  };
  currentTab: 'tous' | 'pending' | 'rejected' | 'accepted' = 'tous';

  requests: any[] = [];
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  filterStatus = ''; // For filtering by status
  errorMessage = '';
  successMessage = '';
  catalogue: any[] = [];
  formations: any[] = [];

  catalogueSelected = false;  // To track if catalogue is selected
  competences: any[] = [];
  filterCompetence: string = ''; // For filtering by target competence

  modifyForm!: FormGroup;
  selectedRequest: any = null;

  // For state management
  demandeState$!: Observable<{ appState: string, appData?: ApiResponse<Page>, error?: HttpErrorResponse }>;
  responseSubject = new BehaviorSubject<ApiResponse<Page>>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();

  constructor(
    private modalService: NgbModal,
    private fb: FormBuilder,
    private demandeFormationService: DemandeFormationServiceService,
    private catalogueService: CatalogueServiceService,
    private dialog:MatDialog,
    private tokenStorage: TokenStorageService,
    private router:Router,
    private competenceService:CompetenceService
  ) { }

  ngOnInit(): void {
    this.loadDemandes(); // Initial load
    this.loadCatalogues();
    this.loadCompetences();


    // Add 'catalogue' to the form group
    this.modifyForm = this.fb.group({
      catalogue: ['', Validators.required],  // Catalogue control added here
      formationTitle: [{ value: '', disabled: true }, Validators.required],  // The formation title
      justification: ['', [Validators.required]],  // Justification
      status: ['', [Validators.required]]  // Status field (if you still need it)
    });
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
  // Load the demandes with pagination and status filter
  loadDemandes(): void {
    const user = this.tokenStorage.getUser();

    let status = this.filterStatus ? this.filterStatus : this.getStatusFromTab(this.currentTab);

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
    // If status is empty, use API without the status parameter
    let apiCall: Observable<ApiResponse<Page>>;

    apiCall = this.demandeFormationService.getHistoryOfTraining(user.id,status, competenceId, this.currentPageSubject.value);

    this.demandeState$ = apiCall.pipe(
      map(response => {
        console.log('Demandes response:', response);
        this.responseSubject.next(response);
        this.currentPageSubject.next(response.data.page.number);
        return { appState: 'APP_LOADED', appData: response };
      }),
      startWith({ appState: 'APP_LOADING' }),
      catchError(error => {
        console.error('Error loading demandes:', error);
        return of({ appState: 'APP_ERROR', error });
      })
    );
  }

  onStatusChange($event: Event): void {
    this.filterStatus = ($event.target as HTMLSelectElement).value;
    this.loadDemandes();
  }

  selectTab(tab: 'tous' | 'pending' | 'rejected' | 'accepted'): void {
    if (this.currentTab !== tab) {
      this.currentTab = tab;
      this.filterStatus = ''; // Clear filter status when switching tabs
      this.loadDemandes();
    }
  }


  // Filter the demandes by the selected status
  filterDemandesByStatus(): void {
    this.currentPage = 0; // Reset to first page when filtering
    this.loadDemandes(); // Reload the demandes with the selected status
  }

  getStatusFromTab(tab: 'tous' | 'pending' | 'accepted' | 'rejected'): string {
    if (tab === 'tous') {
      return ''; // Empty string to fetch all requests
    } else if (tab === 'pending') {
      return 'PENDING';
    } else if (tab === 'accepted') {
      return 'ACCEPTED_MANAGER';
    } else if (tab === 'rejected') {
      return 'REJECTED';
    }
    return ''; // Default empty status
  }
  // Approve a demande
  approveDemande(requestId: number): void {
    this.demandeFormationService.approveDemande(requestId).subscribe(
      response => {
        this.successMessage = 'Demande approuvée avec succès';
        this.loadDemandes(); // Reload the list after approval
      },
      error => {
        this.errorMessage = 'Erreur lors de l\'approbation de la demande';
        console.error('Error:', error);
      }
    );
  }

  // Reject a demande


  // Delete a demande
  deleteDemande(requestId: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette demande?')) {
      this.demandeFormationService.deleteRequest(requestId).subscribe(
        response => {
          this.successMessage = 'Demande supprimée avec succès';
          this.loadDemandes(); // Reload the list after deletion
        },
        error => {
          this.errorMessage = 'Erreur lors de la suppression de la demande';
          console.error('Error:', error);
        }
      );
    }
  }
  filterDemandes(): void {
    this.loadDemandes(); // Reset to the first page when applying filters
  }

  // Navigate to the specified page
  goToPage(page: number): void {
    this.currentPage = page;
    this.loadDemandes(); // Reload demandes for the new page
  }

  // Navigate to the next or previous page
  goToNextOrPreviousPage(direction: string): void {
    if (direction === 'forward' && this.currentPage < this.totalPages - 1) {
      this.currentPage++;
    } else if (direction === 'backward' && this.currentPage > 0) {
      this.currentPage--;
    }
    this.loadDemandes();
  }

  // Open Modify Modal
  openModifyModal(requestId: number, content: any): void {
    this.demandeFormationService.getRequestById(requestId).subscribe(
      (response) => {
        this.selectedRequest = response.data;

        console.log(this.selectedRequest);

        // Check if formation and catalogue exist before trying to access their properties
        const catalogueId = this.selectedRequest?.formation?.catalogue?.id || null;
        const formationId = this.selectedRequest?.formation?.id || null;

        // Pre-fill the form with request data
        this.modifyForm.patchValue({
          catalogue: catalogueId,
          formationTitle: formationId,
          justification: this.selectedRequest.justification || ''
        });

        // Load formations based on the selected catalogue
        if (catalogueId) {
          this.onCatalogueChange();
        }

        // Open the modal
        this.modalService.open(content, { centered: true });
      },
      (error) => {
        console.error('Error fetching request details:', error);
      }
    );
  }

  // Submit modified request
  submitModifiedRequest(): void {
    if (this.modifyForm.valid) {
      const modifiedRequest = {
        justification: this.modifyForm.get('justification')?.value,
        formationId: this.modifyForm.get('formationTitle')?.value
      };

      // Call the service to update the request (assuming you pass the ID as well)
      this.demandeFormationService.updateRequest(this.selectedRequest.id, modifiedRequest).subscribe(
        response => {
          this.successMessage = 'Demande modifiée avec succès';
          this.loadDemandes();  // Reload the list after modification
          this.modalService.dismissAll(); // Close the modal after successful update
        },
        error => {
          this.errorMessage = 'Erreur lors de la modification de la demande';
          console.error('Error updating request:', error);
        }
      );
    }
  }

  // Load catalogues from the service
  loadCatalogues(): void {
    this.catalogueService.getAllCatalogues().subscribe(
      (response: any) => {
        console.log("catalogues", response);

        // Correctly access the catalogues array within response.data
        if (response.data && Array.isArray(response.data.catalogues)) {
          this.catalogue = response.data.catalogues;  // Assign catalogue array
        } else {
          console.error('Expected an array, but got', typeof response.data);
          this.catalogue = [];  // Set to an empty array if not valid
        }
      },
      (error: any) => {
        console.error('Error loading catalogues:', error);
        this.catalogue = [];  // In case of error, ensure the value is an array
      }
    );
  }

  // Load formations based on selected catalogue
  onCatalogueChange(): void {
    const catalogueId = this.modifyForm.get('catalogue')?.value;  // Get selected catalogueId

    if (catalogueId) {
      this.catalogueSelected = true;
      this.catalogueService.getTrainingsByCatalogue(catalogueId).subscribe(
        (response: any) => {
          console.log("Full response from API: ", response);  // Log the full response

          // Access formations from the path: response.data[""].content
          if (response?.data?.[""]?.content && Array.isArray(response.data[""].content)) {
            this.formations = response.data[""].content;  // Assign formations array
          } else {
            console.error('Expected an array, but got:', typeof response.data);
            this.formations = [];  // Handle invalid format or missing data
          }

          this.modifyForm.get('formationTitle')?.enable();  // Enable the formation dropdown
        },
        (error: any) => {
          this.errorMessage = 'Erreur lors du chargement des formations';
          console.error('Error loading formations:', error);
          this.formations = [];  // Ensure it's an array even in case of error
        }
      );
    } else {
      this.catalogueSelected = false;
      this.formations = [];
      this.modifyForm.get('formationTitle')?.disable();  // Disable the formation dropdown if no catalogue is selected
    }
  }
  closeModal(): void {
    this.modalService.dismissAll();
  }

  getMappedStatus(status: string): string {
    return this.statusMapping[status]?.displayName || status;
  }

  getStatusClass(status: string): string {
    return this.statusMapping[status]?.style || '';
  }
  openDemandeDetail(requestId: number): void {
    this.demandeFormationService.getRequestById(requestId).subscribe(
      response => {
        this.selectedRequest = response.data;

        // Open the modal and pass the selected request
        const dialogRef = this.dialog.open(DemandeModifierComponent, {
          width: '500px',
          data: { selectedRequest: this.selectedRequest }
        });

        dialogRef.afterClosed().subscribe((result) => {
          if (result) {
            this.successMessage = 'La demande a été modifiée avec succès.';
            this.loadDemandes(); // Reload demandes after modification
          }
        });
      },
      error => {
        console.error('Error fetching request details:', error);
      }
    );
  }
  redirectToAddDemande() {
    this.router.navigate(['AjoutDemandeFormation'])
  }
}
