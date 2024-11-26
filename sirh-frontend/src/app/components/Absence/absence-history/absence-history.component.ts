import { Component, OnInit, TemplateRef } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, startWith } from "rxjs";
import { HttpErrorResponse } from "@angular/common/http";
import { AbsenceService } from "../../../_services/absence.service";
import { MatDialog } from "@angular/material/dialog";
import { ApiResponse } from "../../../_helpers/api-response";
import { Page } from "../../../_helpers/page";
import { TokenStorageService } from "../../../_services/token-storage.service";
import { DomSanitizer, SafeUrl } from "@angular/platform-browser";
import { Router } from "@angular/router";
import { ConfirmModalComponent } from "../../Formations/Catalogue/confirm-modal/confirm-modal.component";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-absence-history',
  templateUrl: './absence-history.component.html',
  styleUrls: ['./absence-history.component.scss']
})
export class AbsenceHistoryComponent implements OnInit {

  // Mapping for absence types
  private absenceTypeMapping: { [key: string]: string } = {
    'PAID_LEAVE': 'Congé payé',
    'UNPAID_LEAVE': 'Congé non payé',
    'SICK_LEAVE': 'Congé maladie',
    'MATERNITY_LEAVE': 'Congé maternité',
    'PATERNITY_LEAVE': 'Congé paternité',
    'PARENTAL_LEAVE': 'Congé parental',
    'COMPASSIONATE_LEAVE': 'Congé de compassion',
    'STUDY_LEAVE': 'Congé d\'études',
    'SABBATICAL_LEAVE': 'Congé sabbatique',
    'MARRIAGE_LEAVE': 'Congé de mariage',
    'ADOPTION_LEAVE': 'Congé d\'adoption',
    'PUBLIC_HOLIDAYS': 'Jours fériés',
    'MEDICAL_LEAVE': 'Congé médical',
    'BEREAVEMENT_LEAVE': 'Congé de deuil',
    'EMERGENCY_LEAVE': 'Congé d\'urgence'
  };

  // Mapping for status display
  statusMapping: { [key: string]: { displayName: string, style: string } } = {
    'PENDING': { displayName: 'En attente', style: 'badge rounded-pill badge-warning text-dark px-3 py-2' },
    'VALID': { displayName: 'Validé', style: 'badge rounded-pill badge-success text-light px-3 py-2' },
    'REJECTED': { displayName: 'Refusé', style: 'badge rounded-pill badge-danger text-light px-3 py-2' }
  };

  private demandeIdToReject: number | null = null;
  currentTab: 'tous' | 'pending' | 'rejected' | 'accepted' = 'tous';

  absenceState$: Observable<{ appState: string, appData?: ApiResponse<Page>, error?: HttpErrorResponse }>;
  responseSubject = new BehaviorSubject<ApiResponse<Page>>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();

  successMessage: string;
  errorMessage: string;
  filterStatus: string = '';

  // Filters
  filterAbsenceType: string = '';
  startDate: string = '';
  endDate: string = '';
  absenceTypes: { value: string, display: string }[] = [];

  modalImageSrc: SafeUrl; // For modal image display

  constructor(
    private absenceService: AbsenceService,
    private dialog: MatDialog,
    private tokenStorage: TokenStorageService,
    private sanitizer: DomSanitizer,
    private modalService: NgbModal,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Initialize absenceTypes from absenceTypeMapping
    this.absenceTypes = Object.keys(this.absenceTypeMapping).map(key => {
      return { value: key, display: this.absenceTypeMapping[key] };
    });
    this.loadTabData();
  }

  // Method called when any filter changes
  onFilterChange(): void {
    // Validate date range
    if (this.startDate && this.endDate && this.startDate > this.endDate) {
      this.errorMessage = "La date de début ne peut pas être après la date de fin.";
      return;
    } else {
      this.errorMessage = '';
    }
    this.currentPageSubject.next(0); // Reset to first page when filters change
    this.loadTabData();
  }

  // Method to reset filters
  resetFilters(): void {
    this.filterStatus = '';
    this.filterAbsenceType = '';
    this.startDate = '';
    this.endDate = '';
    this.onFilterChange();
  }

  // Load data with current filters
  loadTabData(): void {
    const status = this.filterStatus ? this.filterStatus : this.getStatusFromTab(this.currentTab);

    let apiCall: Observable<ApiResponse<Page>>;
    apiCall = this.absenceService.getMyAbsencesWithFilters(
      this.currentPageSubject.value,
      status,
      this.filterAbsenceType,
      this.startDate,
      this.endDate
    );

    this.absenceState$ = apiCall.pipe(
      map((response: ApiResponse<Page>) => {
        console.log(response);
        this.responseSubject.next(response);
        this.currentPageSubject.next(response.data.page.number);
        return { appState: 'APP_LOADED', appData: response };
      }),
      startWith({ appState: 'APP_LOADING' }),
      catchError((error: HttpErrorResponse) => {
        console.error('Error loading absences:', error);
        return of({ appState: 'APP_ERROR', error });
      })
    );
  }

  // Map tab to status
  getStatusFromTab(tab: 'tous' | 'pending' | 'accepted' | 'rejected'): string {
    if (tab === 'tous') {
      return ''; // Empty string to fetch all requests
    } else if (tab === 'pending') {
      return 'PENDING';
    } else if (tab === 'accepted') {
      return 'VALID';
    } else if (tab === 'rejected') {
      return 'REJECTED';
    }
    return ''; // Default empty status
  }

  // Navigation methods
  goToPage(name?: string, pageNumber: number = 0): void {
    this.currentPageSubject.next(pageNumber);
    this.loadTabData();
  }

  goToNextOrPreviousPage(direction?: string, name?: string): void {
    this.goToPage(name, direction === 'forward' ? this.currentPageSubject.value + 1 : this.currentPageSubject.value - 1);
  }

  // Mapping methods
  getMappedAbsenceType(absenceType: string): string {
    return this.absenceTypeMapping[absenceType] || absenceType;
  }

  getMappedStatus(status: string): string {
    return this.statusMapping[status]?.displayName || status;
  }

  getStatusClass(status: string): string {
    return this.statusMapping[status]?.style || '';
  }

  // Method to add a new absence request
  AjoutDemande() {
    this.router.navigate(['/AjoutAbsence']);
  }

  // Method to delete an absence
  deleteAbsence(id: number) {
    const modalRef = this.modalService.open(ConfirmModalComponent);
    modalRef.componentInstance.message = 'Êtes-vous sûr de vouloir supprimer cette demande d\'absence ?';

    modalRef.result.then(
      (confirmed) => {
        if (confirmed) {
          this.absenceService.deleteAbsence(id).subscribe(
            (response) => {
              this.successMessage = 'Demande d\'absence supprimée avec succès.';
              this.loadTabData();
              setTimeout(() => this.successMessage = '', 4000);
            },
            (error) => {
              this.errorMessage = 'Erreur lors de la suppression de la demande d\'absence.';
            }
          );
        }
      },
      (reason) => {
        console.log('Modal dismissed:', reason);
      }
    );
  }

  // Method to open the justificatif modal
  openJustificatifModal(absence: any, content: TemplateRef<any>) {
    this.modalImageSrc = this.getSafeUrl(absence.justificatifPath);
    this.modalService.open(content, { size: 'lg' });
  }

  // Get safe URL for image display
  getSafeUrl(url: string): SafeUrl {
    const completeUrl = `http://localhost:8085${url}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(completeUrl);
  }

  // Placeholder for update method
  updateAbsence(id: number) {
    // Implement update functionality if needed
  }
}
