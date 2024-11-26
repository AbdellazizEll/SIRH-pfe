import { Component, OnInit } from '@angular/core';
import { FormationsServiceService } from "../../../_services/formations-service.service";
import { TokenStorageService } from "../../../_services/token-storage.service";
import { CompetenceService } from "../../../_services/competence.service";
import { ChangeDetectorRef } from '@angular/core';
import {DemandeFormationServiceService} from "../../../_services/demande-formation-service.service";
import {ConfirmDialogComponent} from "../../Absence/confirm-dialog/confirm-dialog.component";
import {Dialog} from "@angular/cdk/dialog";
import {MatDialog} from "@angular/material/dialog";

declare var bootstrap: any;

@Component({
  selector: 'app-suggestion-formation',
  templateUrl: './suggestion-formation.component.html',
  styleUrls: ['./suggestion-formation.component.scss']
})
export class SuggestionFormationComponent implements OnInit {
  suggestedTrainings: any[] = [];
  errorMessage: string = '';
  successMessage: string = '';
  userInfo: any;
  competencies: any = [];
  selectedCompetenceId: number | null = null;
  selectedCurrentLevel: number | null = null;
  selectedTargetLevel: number | null = null;
  levels: number[] = [1, 2, 3, 4, 5];

  // Modal-related variables
  selectedTraining: any = null;
  selectedTrainingIndex: number | null = null;

  constructor(
    private trainingService: FormationsServiceService,
    private tokenStorage: TokenStorageService,
    private competenceService: CompetenceService,
    private cdr: ChangeDetectorRef,
    private demandeFormation:DemandeFormationServiceService,
    private dialog:MatDialog
  ) {}

  ngOnInit(): void {
    this.userInfo = this.tokenStorage.getUser();
    this.loadCompetencies();
    this.loadSuggestedTrainings();
  }

  loadCompetencies(): void {
    this.competenceService.getAllCompetences().subscribe(
      (response) => {
        console.log('Competences response:', response);
        this.competencies = Array.isArray(response.data?.page?.content) ? response.data.page.content : [];
      },
      (error) => {
        console.error('Error loading competences:', error);
      }
    );
  }


  loadSuggestedTrainings(): void {
    this.trainingService.getTeamSuggestedTraining(
      this.userInfo.id,
      this.selectedCompetenceId,
      this.selectedCurrentLevel,
      this.selectedTargetLevel
    ).subscribe(
      data => {
        if (Array.isArray(data)) {
          this.suggestedTrainings = data;
        } else if (data && data.page && Array.isArray(data.page.content)) {
          // Handle cases where data might be wrapped inside a `page` object
          this.suggestedTrainings = data.page.content;
        } else {
          this.suggestedTrainings = []; // Set to empty array if data is not in expected format
          console.warn("Unexpected data format:", data);
        }
        console.log("Suggested Trainings:", this.suggestedTrainings);
      },
      error => {
        console.error('Error fetching team suggested trainings', error);
        this.errorMessage = 'Erreur lors de la récupération des formations suggérées pour l\'équipe.';
      }
    );
  }

  onCompetenceChange(): void {
    this.loadSuggestedTrainings();
  }

  onLevelChange(): void {
    this.loadSuggestedTrainings();
  }

  clearFilter(): void {
    this.selectedCompetenceId = null;
    this.selectedCurrentLevel = null;
    this.selectedTargetLevel = null;
    this.loadSuggestedTrainings();
  }

  // Open modal to confirm the request
  openRequestModal(training: any, index: number): void {
    this.selectedTraining = training;
    this.selectedTrainingIndex = index;
    const requestModal = new bootstrap.Modal(document.getElementById('requestModal'), {
      keyboard: false
    });
    requestModal.show();
  }

  closeModal(): void {
    const requestModal = bootstrap.Modal.getInstance(document.getElementById('requestModal'));
    if (requestModal) {
      requestModal.hide();
    }
  }

  // Confirm and send the request to the backend
  confirmRequest(training: any, index: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: "Êtes-vous sûr de vouloir passer une demande pour cette formation ?" }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        const requestBody = {
          formationId: training.formationId,
          managerId: this.userInfo.id,
          collaboratorId: training.targetedCollaborator.id,
          justification: "Requested by manager"
        };

        this.demandeFormation.createRequestFromSuggested(requestBody).subscribe(
          response => {
            this.successMessage = "Demande créée avec succès !";

            // Remove the training from the list
            this.suggestedTrainings.splice(index, 1);

            this.loadSuggestedTrainings();
            },
          error => {
            this.errorMessage = error.error?.details || "Une erreur s'est produite lors de la création de la demande.";
          }
        );
      }
    });
  }

}
