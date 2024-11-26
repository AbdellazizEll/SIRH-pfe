import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from "@angular/material/dialog";
import { CollaboratorService } from "../../../../_services/collaborator.service";
import { PosteService } from "../../../../_services/poste.service";
import { ConfirmDialogComponent } from "../../../Absence/confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-assign-collab',
  templateUrl: './assign-collab.component.html',
  styleUrls: ['./assign-collab.component.scss']
})
export class AssignCollabComponent implements OnInit {
  assignForm: FormGroup;
  joblessCollaborators: any[] = [];
  assignedCollaborators: any[] = [];
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    public dialogRef: MatDialogRef<AssignCollabComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    private collaboratorService: CollaboratorService,
    private posteService: PosteService,
    private dialog: MatDialog
  ) {
    this.assignForm = this.fb.group({
      collaborateurId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadJoblessCollaborators();
    this.loadAssignedCollaborators();
  }

  // Load jobless collaborators (available for assignment)
  loadJoblessCollaborators(): void {
    this.collaboratorService.fetchJoblessCollaborators().subscribe(
      response => {
        this.joblessCollaborators = response;
      },
      error => {
        console.error('Error fetching jobless collaborators:', error);
      }
    );
  }

  // Load assigned collaborators using posteId
  loadAssignedCollaborators(): void {
    this.posteService.getPosteById(this.data.posteId).subscribe(
      (response: any) => {
        this.assignedCollaborators = response.assignedCollaborators || [];
      },
      error => {
        console.error('Error fetching assigned collaborators:', error);
      }
    );
  }

  // Assign a new collaborator to the post
  assignCollaborator(): void {
    const collaboratorId = this.assignForm.value.collaborateurId;

    // Check if a collaborator has been selected
    if (!collaboratorId) {
      alert("No collaborator selected.");
      return;
    }

    this.posteService.assignPoste(collaboratorId, this.data.posteId).subscribe(
      () => {
        this.successMessage = 'Collaborateur assigné avec succès';
        this.loadJoblessCollaborators();
        this.loadAssignedCollaborators();
      },
      error => {
        this.errorMessage = 'Erreur lors de l’assignation du collaborateur';
        console.error('Error assigning collaborator:', error);
      }
    );
  }

  // Remove a collaborator from the post
  removeCollaborator(collaboratorId: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: "Êtes-vous sûr de vouloir retirer ce collaborateur ?" },
      // Ensure confirmation dialog appears on top
      panelClass: 'confirm-dialog-container'
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.posteService.unassignCollaborator(collaboratorId, this.data.posteId).subscribe(
          () => {
            this.successMessage = 'Collaborateur retiré avec succès';
            this.loadAssignedCollaborators();
          },
          error => {
            this.errorMessage = 'Erreur lors de la suppression du collaborateur';
            console.error('Error removing collaborator:', error);
          }
        );
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }
}
