import { Component, OnInit } from '@angular/core';
import { CollaboratorService } from "../../../_services/collaborator.service";
import { FormationsServiceService } from "../../../_services/formations-service.service";
import { DemandeFormationServiceService } from "../../../_services/demande-formation-service.service";
import { CatalogueServiceService } from '../../../_services/catalogue-service.service';
import { CompetenceService } from '../../../_services/competence.service';
import {TokenStorageService} from "../../../_services/token-storage.service";  // Import Competence Service

@Component({
  selector: 'app-demande-formation',
  templateUrl: './demande-formation.component.html',
  styleUrls: ['./demande-formation.component.scss']
})
export class DemandeFormationComponent implements OnInit {
  employees: any[] = [];
  competences: any = [];
  selectedEmployee: any = null;
  selectedCatalogue: any = null;
  selectedTraining: any = null;
  selectedTargetCompetence: any = null;
  trainings: any[] = [];
  catalogues: any[] = [];
  justification: string = '';
  isCustomized: boolean = false;

  // Customized training fields
  customTitle: string = '';
  customDescription: string = '';
  customStartDate: string = '';
  customEndDate: string = '';
  customCurrentLevel: number = 0;  // For current level input
  customTargetLevel: number = 0;   // For target level input
  userInfo: any;  // User information

  // For showing validation and success messages
  errorMessage: string = '';
  successMessage: string = '';
  collaborators: any = [];  // List of sub-collaborators (team members)

  constructor(
    private collaboratorService: CollaboratorService,
    private formationService: FormationsServiceService,
    private demandeFormationsService: DemandeFormationServiceService,
    private catalogueService: CatalogueServiceService,
    private competenceService: CompetenceService,
    private tokenStorage: TokenStorageService,
  ) {}

  ngOnInit(): void {
    this.userInfo = this.tokenStorage.getUser();
    this.findCollaboratorById(); // Load manager and collaborators
    this.loadCatalogues();
    this.loadCompetences();
  }

  findCollaboratorById(): void {
    this.collaboratorService.findById(this.userInfo.id).subscribe(
      data => {
        // Assuming the `managerEquipe` contains the sub-collaborators array
        if (data && data.managerEquipe && data.managerEquipe.subCollaborators) {
          this.collaborators = data.managerEquipe.subCollaborators;
          // Map collaborators to the employee list format
          this.employees = this.collaborators.map((collab: any) => ({
            id: collab.id,
            name: `${collab.firstname} ${collab.lastname}`
          }));
        } else {
          console.error('ManagerEquipe or subCollaborators data not available');
        }
      },
      error => {
        console.error('Error fetching collaborator details', error);
      }
    );
  }

  // Submit the form and validate
  submitRequest() {
    this.errorMessage = '';
    this.successMessage = '';

    // Validate form based on whether it's classic or customized
    if (!this.selectedEmployee || !this.justification.trim()) {
      this.errorMessage = 'Tous les champs obligatoires doivent être remplis.';
      return;
    }

    let requestBody: any;

    if (this.isCustomized) {
      // Customized Training Request (No changes here)
      if (!this.customTitle || !this.customDescription || !this.customStartDate || !this.customEndDate ||
        !this.selectedCatalogue || !this.selectedTargetCompetence || !this.customCurrentLevel || !this.customTargetLevel) {
        this.errorMessage = 'Tous les champs personnalisés sont obligatoires pour une formation personnalisée.';
        return;
      }

      requestBody = {
        request: {
          employee: {
            id: this.selectedEmployee
          },
          status: 'PENDING'
        },
        formation: {
          title: this.customTitle,
          description: this.customDescription,
          targetCompetence: {
            id: this.selectedTargetCompetence
          },
          currentLevel: this.customCurrentLevel,
          targetLevel: this.customTargetLevel,
          startingAt: this.customStartDate,
          finishingAt: this.customEndDate,
          catalogue: {
            id: this.selectedCatalogue
          }
        }
      };

      // Trigger customized request
      this.demandeFormationsService.createCustomRequest(requestBody).subscribe(
        (response) => {
          this.successMessage = 'La demande personnalisée a été soumise avec succès.';
          this.clearForm();
        },
        (error) => {
          this.errorMessage = 'Une erreur s\'est produite lors de la soumission de la demande personnalisée.';
          console.error('Error submitting customized request:', error);
        }
      );

    } else {
      // Classic Training Request (Changes made here)
      if (!this.selectedCatalogue || !this.selectedTraining) {
        this.errorMessage = 'Veuillez sélectionner une formation et un catalogue.';
        return;
      }

      // Adjusted request body for the classic request to match backend expectations
      requestBody = {
        formation: {
          id: this.selectedTraining
        },
        employee: {
          id: this.selectedEmployee
        },
        justification: this.justification
      };

      // Trigger classic request
      this.demandeFormationsService.createDemande(requestBody).subscribe(
        (response) => {
          this.successMessage = 'La demande a été soumise avec succès.';
          this.clearForm();
        },
        (error) => {
          console.error('Error:', error);
          this.errorMessage = 'Une erreur s\'est produite lors de la soumission de la demande.';
        }
      );
    }
  }

  // Load competences
  private loadCompetences() {
    this.competenceService.getAllCompetences().subscribe(
      (response) => {
        this.competences = response;
      },
      (error) => {
        console.error('Error loading competences:', error);
      }
    );
  }

  // Load formations when catalogue changes
  onCatalogueChange() {
    if (this.selectedCatalogue) {
      this.formationService.getFormationsByCatalogue(this.selectedCatalogue).subscribe(
        (response) => {
          // Correctly accessing the formations from response.data.page.content
          this.trainings = response.data?.page?.content || [];
          console.log(this.trainings);  // Log the extracted formations
        },
        (error) => {
          this.trainings = [];
          this.errorMessage = 'Une erreur s\'est produite lors du chargement des formations.';
          console.error('Error loading formations:', error);
        }
      );
    } else {
      this.trainings = [];
    }
  }

  private loadCatalogues() {
    this.catalogueService.getAllCatalogues().subscribe(
      (response) => {
        this.catalogues = response.data?.catalogues || [];
      },
      (error) => {
        this.errorMessage = 'Une erreur s\'est produite lors du chargement des catalogues.';
        console.error('Error loading catalogues:', error);
      }
    );
  }

  // Reset the form after submission
  private clearForm() {
    this.selectedEmployee = null;
    this.selectedCatalogue = null;
    this.selectedTraining = null;
    this.selectedTargetCompetence = null;
    this.justification = '';
    this.customTitle = '';
    this.customDescription = '';
    this.customStartDate = '';
    this.customEndDate = '';
    this.trainings = [];
    this.isCustomized = false;
    this.customCurrentLevel = 0;
    this.customTargetLevel = 0;
  }

  onCancel() {
    // Action for canceling the form
  }

  redirectToDemande() {
    // Action for redirecting
  }
}
