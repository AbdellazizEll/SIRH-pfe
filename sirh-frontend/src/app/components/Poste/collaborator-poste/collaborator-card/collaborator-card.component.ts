import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CollaboratorService } from '../../../../_services/collaborator.service';
import { PosteService } from '../../../../_services/poste.service';

@Component({
  selector: 'app-collaborator-card',
  templateUrl: './collaborator-card.component.html',
  styleUrls: ['./collaborator-card.component.scss']
})
export class CollaboratorCardComponent implements OnInit {
  collaborator: any;
  poste: any;

  constructor(
    public dialogRef: MatDialogRef<CollaboratorCardComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private collaboratorService: CollaboratorService,
    private posteService: PosteService
  ) { }

  ngOnInit(): void {
    this.getCollaboratorInfo(this.data.collaboratorId);
  }

  getCollaboratorInfo(id: number): void {
    this.collaboratorService.findById(id).subscribe(
      data => {
        this.collaborator = data;
        this.getPosteInfo(this.collaborator.posteOccupe.idPoste);
      },
      error => console.error('Error fetching collaborator details', error)
    );
  }

  getPosteInfo(id: number): void {
    this.posteService.getPosteById(id).subscribe(
      data => {
        this.poste = data;
        console.log('Poste data:', this.poste);
      },
      error => console.error('Error fetching poste details', error)
    );
  }

  close(): void {
    this.dialogRef.close();
  }

  mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      'English': 'Anglais',
      'French': 'Français',
      'JAVA': 'Java',
      'Soft Skills': 'Compétences Douces'
      // Add more mappings as necessary
    };
    return competenceMap[name] || name;
  }

  mapEvaluationValue(value: string, scaleType: string): string {
    const evaluationMap: { [key: string]: string } = {
      '1 STAR': '1 Étoile',
      '2 STARS': '2 Étoiles',
      '3 STARS': '3 Étoiles',
      '4 STARS': '4 Étoiles',
      'EXCELLENT': 'Excellent',
      'BON': 'Bon',
      'MOYEN': 'Moyen',
      'FAIBLE': 'Faible'
      // Add more mappings as necessary
    };
    if (scaleType === 'NUMERIC') {
      return value;
    } else {
      return evaluationMap[value] || value;
    }
  }
}
