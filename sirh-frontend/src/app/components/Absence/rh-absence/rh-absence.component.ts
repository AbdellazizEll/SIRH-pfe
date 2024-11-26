import {Component, OnInit, TemplateRef} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {ApiResponse} from "../../../_helpers/api-response";
import {Page} from "../../../_helpers/page";
import {HttpErrorResponse} from "@angular/common/http";
import {AbsenceService} from "../../../_services/absence.service";
import {MatDialog} from "@angular/material/dialog";
import {NgForm} from "@angular/forms";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {TokenStorageService} from "../../../_services/token-storage.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {RejectionDialogComponent} from "../rejection-dialog/rejection-dialog.component";

@Component({
  selector: 'app-rh-absence',
  templateUrl: './rh-absence.component.html',
  styleUrls: ['./rh-absence.component.scss']
})
export class RhAbsenceComponent implements OnInit {
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
  statusMapping: { [key: string]: { displayName: string, style: string } } = {
    'PENDING': { displayName: 'En attente', style: 'badge rounded-pill badge-warning text-dark px-3 py-2' },
    'VALID': { displayName: 'Validé', style: 'badge rounded-pill badge-success text-light px-3 py-2' },
    'REJECTED': { displayName: 'Refusé', style: 'badge rounded-pill badge-danger text-light px-3 py-2' },
    'ACCEPTED_MANAGER': { displayName: 'Accepté par Manager', style: 'badge rounded-pill badge-info text-light px-3 py-2' },
    'ACCEPTED_RESPONSABLE': { displayName: 'Accepté par Responsable', style: 'badge rounded-pill badge-primary text-light px-3 py-2' },
    'ACCEPTED_RH': { displayName: 'Accepté par RH', style: 'badge rounded-pill badge-success text-light px-3 py-2' },
    'TREATING': { displayName: 'En traitement', style: 'badge rounded-pill badge-secondary text-light px-3 py-2' }
  };

  private demandeIdToReject: number | null = null;
  currentTab: 'tous' | 'pending' | 'rejected' | 'accepted' = 'tous';

  absenceState$: Observable<{ appState: string, appData?: ApiResponse<Page>, error?: HttpErrorResponse }>;
  responseSubject = new BehaviorSubject<ApiResponse<Page>>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();
  modalImageSrc: SafeUrl;

  successMessage: string;
  errorMessage: string;
  filterStatus: string = '';

  filterAbsenceType: string = '';
  startDate: string = '';
  endDate: string = '';
  absenceTypes: { value: string, display: string }[] = [];
  refusalReason: any;

  constructor(private absenceService: AbsenceService, private dialog: MatDialog,
              private tokenStorage: TokenStorageService,
              private sanitizer: DomSanitizer,
              private modalService: NgbModal,
  ) { }

  ngOnInit(): void {
    // Initialize absenceTypes
    this.absenceTypes = Object.keys(this.absenceTypeMapping).map(key => {
      return { value: key, display: this.absenceTypeMapping[key] };
    });

    this.loadTabData();
  }

  resetFilters(): void {
    this.filterStatus = '';
    this.filterAbsenceType = '';
    this.startDate = '';
    this.endDate = '';
    this.onFilterChange();
  }

  loadTabData(): void {
    const status = this.filterStatus || this.getStatusFromTab(this.currentTab);

    // Call the API with filters
    this.absenceState$ = this.absenceService.getAbsencesRHWithFilters(
      this.currentPageSubject.value,
      status,
      this.filterAbsenceType,
      this.startDate,
      this.endDate
    ).pipe(
      map((response: ApiResponse<Page>) => {
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

  getStatusFromTab(tab: 'tous' | 'pending' | 'accepted' | 'rejected'): string {
    if (tab === 'tous') {
      return ''; // Empty string to fetch all requests
    } else if (tab === 'pending') {
      return 'PENDING';
    } else if (tab === 'accepted') {
      return 'ACCEPTED_RESPONSABLE';
    } else if (tab === 'rejected') {
      return 'REJECTED';
    }
    return ''; // Default empty status
  }

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

  onStatusChange($event: Event): void {
    this.filterStatus = ($event.target as HTMLSelectElement).value;
    this.loadTabData();
  }

  selectTab(tab: 'tous' | 'pending' | 'rejected' | 'accepted'): void {
    if (this.currentTab !== tab) {
      this.currentTab = tab;
      this.filterStatus = ''; // Clear filter status when switching tabs
      this.loadTabData();
    }
  }

  approveAbsence(demandeId: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: "Êtes-vous sûr de vouloir approuver cette demande d'absence ?" }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        // Call the service to approve the absence
        this.absenceService.approveAbsenceRequestRH(demandeId).subscribe(
          response => {
            console.log("Accepted", response);
            this.errorMessage = '';
            this.successMessage = "Approbation de la demande a été effectuée.";
            setTimeout(() => this.successMessage = '', 4000);

            // Reload data after approval
            this.loadTabData();
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

  getSafeUrl(url: string): SafeUrl {
    const completeUrl = `http://localhost:8085${url}`;
    console.log("Constructed Justificatif URL:", completeUrl); // Debugging log
    return this.sanitizer.bypassSecurityTrustUrl(completeUrl);
  }

  goToPage(name?: string, pageNumber: number = 0): void {
    this.currentPageSubject.next(pageNumber);
    this.loadTabData();
  }

  goToNextOrPreviousPage(direction?: string, name?: string): void {
    this.goToPage(name, direction === 'forward' ? this.currentPageSubject.value + 1 : this.currentPageSubject.value - 1);
  }

  updateRequestState(demandeId: number, newState: 'approved' | 'rejected'): void {
    const currentData = this.responseSubject.value;
    const updatedData = {
      ...currentData,
      data: {
        ...currentData.data,
        page: {
          ...currentData.data.page,
          content: currentData.data.page.content.map(request =>
            request.id === demandeId ? { ...request, state: newState } : request
          )
        }
      }
    };
    this.responseSubject.next(updatedData);
  }

  confirmReject(demandeId: number): void {
    this.demandeIdToReject = demandeId;
  }

  openRejectModal(demandeId: number): void {
    const dialogRef = this.dialog.open(RejectionDialogComponent, {
      width: '400px', // Set dialog width as needed
      data: { demandeId: demandeId }
    });

    dialogRef.afterClosed().subscribe((rejectionReason: string | null) => {
      if (rejectionReason) {
        this.rejectAbsence(demandeId, rejectionReason);
      }
    });
  }

// Method to send the rejection request to the service
  rejectAbsence(demandeId: number, reason: string): void {
    this.absenceService.rejectAbsenceRequest(demandeId, reason).subscribe(
      response => {
        this.successMessage = 'La demande a été refusée avec succès.';
        setTimeout(() => this.successMessage = '', 4000);
        this.loadTabData(); // Reload data to reflect changes
      },
      error => {
        this.errorMessage = 'Le refus de la demande a échoué. Veuillez réessayer.';
        setTimeout(() => this.errorMessage = '', 4000);
      }
    );
  }


  openJustificatifModal(absence: any, content: TemplateRef<any>) {
    this.modalImageSrc = this.getSafeUrl(absence.justificatifPath);
    this.modalService.open(content, { size: 'lg' });
  }

  getMappedAbsenceType(absenceType: string): string {
    return this.absenceTypeMapping[absenceType] || absenceType;
  }

  getMappedStatus(status: string): string {
    return this.statusMapping[status]?.displayName || status;
  }

  getStatusClass(status: string): string {
    console.log('Status Demande:', status);  // Log the status to check what is coming from the backend
    return this.statusMapping[status]?.style || 'badge badge-secondary';  // Fallback to secondary badge if not found
  }

  AjoutDemande() {
    // Logic for adding a new absence request
  }


}
