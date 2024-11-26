import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CollaboratorService } from '../../../_services/collaborator.service';
import { CompetenceService } from '../../../_services/competence.service';

@Component({
  selector: 'app-collab-detail',
  templateUrl: './collab-detail.component.html',
  styleUrls: ['./collab-detail.component.scss']
})
export class CollabDetailComponent implements OnInit {
  collaborator: any;
  competences: any[] = [];

  constructor(
    public dialogRef: MatDialogRef<CollabDetailComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private collaboratorService: CollaboratorService,
    private competenceService: CompetenceService
  ) { }

  ngOnInit(): void {
    const id = this.data.id;
    this.getCollaborator(id);
    this.getCompetences(id);
  }

  getCollaborator(id: number): void {
    this.collaboratorService.findById(id).subscribe(
      response => {
        this.collaborator = response;
        console.log('Collaborator:', this.collaborator);
      },
      error => {
        console.error('Error fetching collaborator', error);
      }
    );
  }

  getCompetences(id: number): void {
    this.competenceService.CurrentUserCompetence(id).subscribe(
      response => {
        this.competences = response;
        console.log('Competences:', this.competences);
      },
      error => {
        console.error('Error fetching competences', error);
      }
    );
  }

  closePopup(): void {
    this.dialogRef.close();
  }
}
