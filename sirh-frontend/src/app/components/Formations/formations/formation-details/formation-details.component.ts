import { Component, OnInit } from '@angular/core';
import { FormationsServiceService } from "../../../../_services/formations-service.service";
import { ActivatedRoute, Router } from "@angular/router";
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CollaboratorService } from "../../../../_services/collaborator.service";
import { TokenStorageService } from "../../../../_services/token-storage.service";
import {DemandeFormationComponent} from "../../demande-formation/demande-formation.component";
import {DemandeFormationServiceService} from "../../../../_services/demande-formation-service.service";

@Component({
  selector: 'app-formation-details',
  templateUrl: './formation-details.component.html',
  styleUrls: ['./formation-details.component.scss']
})
export class FormationDetailsComponent implements OnInit {

  trainingId!: number;
  training: any = {};  // Holds the training details
  collaborators: any[] = [];  // List of sub-collaborators (team members)
  selectedCollaborator: number | null = null;  // Holds the selected collaborator's ID
  errorMessage: string = '';
  successMessage: string = '';

  userInfo: any;  // User information
  collaborator: any;  // Collaborator object containing subCollaborators

  constructor(
    private route: ActivatedRoute,
    private trainingService: FormationsServiceService,
    private router: Router,
    private modalService: NgbModal,
    private collaboratorService: CollaboratorService,
    private tokenStorage: TokenStorageService,
    private demandeFormationService: DemandeFormationServiceService// Token storage to get user details
  ) { }

  ngOnInit(): void {
    this.userInfo = this.tokenStorage.getUser();
    this.findCollaboratorById();
    this.trainingId = this.route.snapshot.params['id'];
    this.getTrainingDetails(this.trainingId);
  }

  // Fetch the training details based on the ID
  getTrainingDetails(trainingId: number): void {
    this.trainingService.getFormationById(trainingId).subscribe(
      (response) => {
        this.training = response.data.formation;
      },
      (error) => {
        this.errorMessage = 'Error loading training details.';
        console.error(error);
      }
    );
  }

  // Load the list of collaborators (sub-collaborators from the manager's team)
  findCollaboratorById(): void {
    this.collaboratorService.findById(this.userInfo.id).subscribe(
      data => {
        this.collaborator = data;

        // Assuming the `subCollaborators` is the array of team members
        this.collaborators = this.collaborator.managerEquipe.subCollaborators;
        console.log('Sub-collaborators: ', this.collaborators);
      },
      error => {
        console.error('Error fetching collaborator details', error);
      }
    );
  }

  // Open the modal using NgbModal
  openModal(content: any): void {
    this.modalService.open(content, { centered: true });
  }

  // Assign collaborator to the selected training
  assignCollaborator(): void {
    if (this.selectedCollaborator) {
      const request = {
        collaboratorId: this.selectedCollaborator,
        formationId: this.trainingId,
      };

      console.log(request)
      // Call the backend service to assign the collaborator
      this.trainingService.assignCollaborator(request).subscribe(
        (response) => {
          console.log(response)
          this.successMessage = 'Collaborator successfully assigned to the training';
          this.modalService.dismissAll();  // Close the modal after assigning
        },
        (error) => {
          this.errorMessage = 'Error assigning collaborator to the training';
          console.error('Error:', error);
        }
      );
    }
  }

  // Navigate back to the catalogue
  goBackToCatalogue(): void {
    const catalogueId = this.training.catalogue?.id;
    if (catalogueId) {
      this.router.navigate([`/ViewCatalogue/${catalogueId}`]);
    }
  }

  // Add to calendar (placeholder)
  addToCalendar(training: any): void {
    alert(`Training added to calendar: ${training.title}`);
  }
}
