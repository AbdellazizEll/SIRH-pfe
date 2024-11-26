import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { EnrollmentServiceService } from '../../../_services/enrollment-service.service';
import { EquipesService } from '../../../_services/equipes.service';
import {BehaviorSubject, forkJoin, Observable, of} from 'rxjs';
import { catchError, map, mergeMap, toArray, startWith } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiResponse } from "../../../_helpers/api-response";
import { Page } from "../../../_helpers/page";
import { TokenStorageService } from '../../../_services/token-storage.service';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {
  CompetenceHistoryModalComponent
} from "../formations/mes-formations/competence-history-modal/competence-history-modal.component";
import {ConfirmModalComponent} from "../Catalogue/confirm-modal/confirm-modal.component";
import {UpdateProgressModalComponent} from "./update-progress-modal/update-progress-modal.component";
import {Router} from "@angular/router";
import {RejectConfirmationModalComponent} from "./reject-confirmation-modal/reject-confirmation-modal.component";

@Component({
  selector: 'app-team-enrollments',
  templateUrl: './team-enrollments.component.html',
  styleUrls: ['./team-enrollments.component.scss']
})
export class TeamEnrollmentsComponent implements OnInit {

  enrollmentsState$: Observable<{ appState: string, appData?: any[], error?: HttpErrorResponse }>; // Adjusted to hold array of enrollments
  responseSubject = new BehaviorSubject<any[]>([]); // Adjusted to hold array of enrollments
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();

  totalPages: number = 0;
  teamMembersIds: number[] = [];  // Store team member IDs here
  selectedCompetenceId: number | null = null;
  selectedCurrentLevel: number | null = null;
  selectedTargetLevel: number | null = null;
  loggedUserId: number | null = null;  // Store the logged user's ID

  enrollments: any = [];

  constructor(
    private enrollmentService: EnrollmentServiceService,
    private equipesService: EquipesService,
    private tokenStorage: TokenStorageService,
    private modalService: NgbModal,
    private router: Router,
    private cdr: ChangeDetectorRef  // Add ChangeDetectorRef here

  ) {
  }

  ngOnInit(): void {
    this.loggedUserId = this.tokenStorage.getUser().id;  // Get the logged-in user's ID
    if (this.loggedUserId) {
      this.loadTeamMembers(this.loggedUserId);  // Load team members for the logged-in manager
    } else {
      console.error("No logged-in user found");
    }
  }

  // Load team members using the logged-in manager's ID
  loadTeamMembers(managerId: number): void {
    this.equipesService.getEquipeByManager(managerId).subscribe(
      response => {
        console.log('Team members response:', response);
        if (response && response.collaborateurs) {
          this.teamMembersIds = response.collaborateurs.map((member: any) => member.id);
          this.loadEnrollmentsForTeam();  // Load enrollments after fetching team members
        } else {
          console.error('No subCollaborators found in response.');
        }
      },
      error => {
        console.error('Error loading team members:', error);
      }
    );
  }

  loadEnrollmentsForTeam(): void {
    this.enrollments = [];  // Clear previous enrollments

    const requests = this.teamMembersIds.map((collaboratorId: number) =>
      this.enrollmentService.getEnrollmentsByCollaborator(collaboratorId)
    );

    // Execute all API requests in parallel and combine the results
    forkJoin(requests).subscribe(
      (responses: any[]) => {
        responses.forEach(response => {
          if (response && response.data && response.data.page && response.data.page.content) {
            this.enrollments.push(...response.data.page.content);  // Append enrollments
          }
        });

        // Now update the enrollmentsState$ observable
        this.enrollmentsState$ = of({
          appState: 'APP_LOADED',
          appData: this.enrollments
        });

        // Now that all enrollments are loaded, you can sort or handle pagination
        console.log('All team enrollments:', this.enrollments);
      },
      error => {
        console.error('Error loading enrollments:', error);
        this.enrollmentsState$ = of({
          appState: 'APP_ERROR',
          error: error
        });
      }
    );
  }

  onFilterChange(): void {
    this.loadEnrollmentsForTeam(); // Reload enrollments when a filter is changed
  }

  goToPage(pageNumber: number): void {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      this.loadEnrollmentsForTeam();
    }
  }

  goToNextOrPreviousPage(direction: string): void {
    const newPage = direction === 'forward' ? this.currentPageSubject.value + 1 : this.currentPageSubject.value - 1;
    if (newPage >= 0 && newPage < this.totalPages) {
      this.loadEnrollmentsForTeam();
    }
  }

  clearFilters(): void {
    this.selectedCompetenceId = null;
    this.selectedCurrentLevel = null;
    this.selectedTargetLevel = null;
    this.loadEnrollmentsForTeam(); // Reload enrollments without filters
  }

  // Open the competence history modal
  openCompetenceHistoryModal(collaboratorId: number): void {
    this.enrollmentService.getCompetenceHistory(collaboratorId).subscribe(
      (data) => {
        const modalRef = this.modalService.open(CompetenceHistoryModalComponent, {size: 'lg'});
        modalRef.componentInstance.competenceHistory = data;
        modalRef.componentInstance.historySuccessMessage = "Historique chargé avec succès!";
      },
      (error) => {
        console.error("Error loading competence history:", error);
      }
    );
  }

  markAsCompleted(enrollment: any): void {
    // Call a service to mark the enrollment as completed
    this.enrollmentService.markAsCompleted(enrollment.id).subscribe(
      () => {
        // Update the enrollment status in the UI
        enrollment.completed = true;
        this.loadEnrollmentsForTeam(); // Refresh the list if necessary
      },
      error => {
        console.error('Error marking enrollment as completed:', error);
      }
    );
  }



  // Method to trigger the modal
  evaluateTraining(enrollmentId: number, isApproved: boolean): void {
    const modalRef = this.modalService.open(RejectConfirmationModalComponent);
    modalRef.componentInstance.message = isApproved
      ? 'Are you sure you want to approve this training?'
      : 'Are you sure you want to reject this training?';
    modalRef.componentInstance.confirmButtonText = isApproved ? 'Approve' : 'Reject';
    modalRef.componentInstance.cancelButtonText = 'Cancel';
    modalRef.componentInstance.isRejection = !isApproved; // Set isRejection based on the action

    modalRef.result.then((result) => {
      const rejectionReason = isApproved ? null : result; // Use reason if rejected
      this.enrollmentService.evaluateTrainingCompletion(enrollmentId, isApproved, rejectionReason).subscribe(
        () => {
          console.log('Training evaluated successfully');
          this.loadEnrollmentsForTeam();
          this.cdr.detectChanges();  // Force a change detection after data is reloaded

        },
        (error) => console.error('Error evaluating training:', error)
      );
    }).catch(() => console.log('Evaluation canceled'));
  }


  openUpdateProgressModal(enrollment: any) {
    const modalRef = this.modalService.open(UpdateProgressModalComponent, { size: 'lg' });

    // Pass the IDs to the modal component via Inputs
    modalRef.componentInstance.collaboratorId = enrollment.collaboratorDTO.id;
    modalRef.componentInstance.formationId = enrollment.formationDTO.id;

    // Handle the result of the modal
    modalRef.result.then((newProgress) => {
      if (newProgress !== null && newProgress >= 0 && newProgress <= 100) {
        this.enrollmentService.updateProgressForTraining(enrollment.collaboratorDTO.id, enrollment.formationDTO.id, newProgress)
          .subscribe(() => {
            this.loadEnrollmentsForTeam(); // Refresh the list after updating
          });
      }
    }).catch((error) => {
      console.error('Modal dismissed without updating progress', error);
    });
  }

  addEnrollment() {
    this.router.navigate(["SuggestionFormation"]);
  }
}
