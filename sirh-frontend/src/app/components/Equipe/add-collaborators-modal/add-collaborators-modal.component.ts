// add-collaborators-modal.component.ts
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CollaboratorService } from '../../../_services/collaborator.service';
import { EquipesService } from '../../../_services/equipes.service';
import { ConfirmDialogComponent } from "../../Absence/confirm-dialog/confirm-dialog.component";
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MatDialog } from "@angular/material/dialog";

@Component({
  selector: 'app-add-collaborators-modal',
  templateUrl: './add-collaborators-modal.component.html',
  styleUrls: ['./add-collaborators-modal.component.scss']
})
export class AddCollaboratorsModalComponent implements OnInit {
  @Input() teamId: number;
  @Input() teamName: string;
  equipeForm: FormGroup;
  collaborators: { id: number; email: string; firstname: string; lastname: string }[] = [];
  assignedCollaborators: any[] = [];
  selectedCollaborators: { id: number; email: string; firstname: string; lastname: string }[] = [];
  errorMessage: string;
  successMessage: string;

  constructor(
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private collaboratorService: CollaboratorService,
    private equipeServ: EquipesService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.equipeForm = this.fb.group({
      selectedCollaborators: [[], Validators.required]
    });

    this.loadCollaborators();
    this.loadAssignedCollaborators();
  }

  private loadCollaborators() {
    this.collaboratorService.fetchTeamlessCollaborators().subscribe(
      response => {
        this.collaborators = response;
        this.removeAlreadyAssignedCollaborators();
      },
      error => {
        console.error('Error loading collaborators:', error);
      }
    );
  }

  private loadAssignedCollaborators() {
    this.equipeServ.getEquipeById(this.teamId).subscribe(
      response => {
        this.assignedCollaborators = response.collaborateurs;
        console.log(this.assignedCollaborators);
        this.removeAlreadyAssignedCollaborators();
      },
      error => {
        console.log('Error fetching assigned collaborators:', error);
      }
    );
  }

  private removeAlreadyAssignedCollaborators() {
    const assignedIds = new Set(this.assignedCollaborators.map(collab => collab.id));
    this.collaborators = this.collaborators.filter(collab => !assignedIds.has(collab.id));
  }

  onCollaboratorSelect(event: any) {
    const selectedId = parseInt(event.target.value, 10);
    const collaborator = this.collaborators.find(c => c.id === selectedId);

    if (collaborator && !this.selectedCollaborators.some(c => c.id === selectedId)) {
      this.selectedCollaborators.push(collaborator);
    }
    event.target.value = ''; // Reset dropdown selection
  }

  removeCollaborator(collaboratorId: number) {
    this.selectedCollaborators = this.selectedCollaborators.filter(c => c.id !== collaboratorId);
  }

  submitForm() {
    const selectedCollaboratorIds = this.selectedCollaborators.map(c => c.id);
    if (selectedCollaboratorIds.length > 0) {
      this.addCollaboratorsToTeam(selectedCollaboratorIds);
    } else {
      alert('No collaborators selected.');
    }
  }

  private addCollaboratorsToTeam(collaboratorIds: number[]) {
    this.equipeServ.assignCollaboratorsToTeam(this.teamId, collaboratorIds).subscribe(
      () => {
        this.successMessage = 'Les collaborateurs ont été ajoutés avec succès';
        this.loadAssignedCollaborators();
        this.loadCollaborators();
        this.selectedCollaborators = [];
        this.errorMessage = '';
        setTimeout(() => {
          this.successMessage = '';
          // Close the modal with 'success' result to indicate that collaborators were added
          this.activeModal.close('success');
        }, 4000);
      },
      error => {
        this.errorMessage = "L'ajout des collaborateurs a échoué";
        this.successMessage = '';
        setTimeout(() => this.errorMessage = '', 4000);
        console.error('Error adding collaborators:', error);
      }
    );
  }

  removeAssignedCollaborator(collaboratorId: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: "Êtes-vous sûr de vouloir retirer ce collaborateur ?" }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.equipeServ.unassignCollaborator(collaboratorId, this.teamId).subscribe(
          () => {
            this.successMessage = 'Collaborateur retiré avec succès';
            this.loadAssignedCollaborators();
            this.loadCollaborators();
            this.errorMessage = '';
            setTimeout(() => this.successMessage = '', 4000);
          },
          error => {
            this.errorMessage = 'Erreur lors de la suppression du collaborateur';
            this.successMessage = '';
            setTimeout(() => this.errorMessage = '', 4000);
            console.error('Error removing collaborator:', error);
          }
        );
      }
    });
  }
}
