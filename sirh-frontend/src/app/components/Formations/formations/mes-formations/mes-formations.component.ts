import { Component, OnInit } from '@angular/core';
import { CollaboratorService } from "../../../../_services/collaborator.service";
import { FormationsServiceService } from "../../../../_services/formations-service.service";
import { EnrollmentServiceService } from "../../../../_services/enrollment-service.service";
import { TokenStorageService } from "../../../../_services/token-storage.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CompetenceHistoryModalComponent} from "./competence-history-modal/competence-history-modal.component";

@Component({
  selector: 'app-mes-formations',
  templateUrl: './mes-formations.component.html',
  styleUrls: ['./mes-formations.component.scss']
})
export class MesFormationsComponent implements OnInit {

  approvedTrainings: any = [];
  competenceHistory: any = [];
  errorMessage: string = '';
  successMessage: string = '';
  historyErrorMessage: string = '';
  historySuccessMessage: string = '';
  user: any;  // Variable to store the user data
  constructor(
    private collaboratorService: CollaboratorService,
    private trainingService: FormationsServiceService,
    private enrollmentService: EnrollmentServiceService,
    private tokenStorage: TokenStorageService,
    private modalService: NgbModal

  ) {}

  ngOnInit(): void {
    // Ensure user data is available before loading the trainings
    this.user = this.tokenStorage.getUser();
    if (this.user && this.user.id) {
      this.loadApprovedTrainings();
    } else {
      this.errorMessage = "Erreur: Utilisateur non trouvé.";
      console.error("User not found or invalid.");
    }
  }

  // Fetch approved trainings for the collaborator
  loadApprovedTrainings(): void {
    if (this.user && this.user.id) {
      this.enrollmentService.getEnrollmentsByCollaborator(this.user.id).subscribe(
        (data) => {
          this.approvedTrainings = data.data.page.content;  // Extract the 'content' from response
          console.log(this.approvedTrainings);
        },
        (error) => {
          this.errorMessage = "Erreur lors du chargement des formations.";
          console.error(error);
        }
      );
    } else {
      this.errorMessage = "Erreur: Utilisateur non trouvé.";
      console.error("User not found or invalid.");
    }
  }

  // View course details
  viewCourse(training: any): void {
    console.log('Viewing course:', training);
    // Implement logic to view course details or redirect to the course platform
  }

  // Continue course logic
  continueCourse(training: any): void {
    console.log('Continuing course:', training);
    // Implement logic to continue the course
  }

  // Mark course as completed
  markAsCompleted(training: any): void {
    if (confirm('Êtes-vous sûr de vouloir marquer cette formation comme terminée ?')) {
      console.log('Marking course as completed:', training);
      // Implement logic to mark the course as completed via the backend API
    }
  }


  viewCompetenceHistory(training: any): void {
    console.log('Viewing competence history for:', training);
    // Implement logic to show competence history
  }

  // Open the competence history modal only if the training is completed
  openCompetenceHistoryModal(training: any): void {
    if (!training.completed) {
      this.historyErrorMessage = "Historique non disponible. La formation n'est pas encore terminée.";
      return;  // Exit if the training is not completed
    }

    // Fetch competence history for the completed training
    this.enrollmentService.getCompetenceHistory(this.user.id).subscribe(
      (data) => {
        // Open the modal and pass the competence history data
        const modalRef = this.modalService.open(CompetenceHistoryModalComponent, { size: 'lg' });
        modalRef.componentInstance.competenceHistory = data;
        modalRef.componentInstance.historySuccessMessage = "Historique chargé avec succès!";
      },
      (error) => {
        this.historyErrorMessage = "Erreur lors du chargement de l'historique de compétence.";
        console.error(error);
      }
    );
  }

}
