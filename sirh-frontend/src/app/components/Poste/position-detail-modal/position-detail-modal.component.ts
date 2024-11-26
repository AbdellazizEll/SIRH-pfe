import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { PosteService } from "../../../_services/poste.service";
import { CompetenceService } from "../../../_services/competence.service";

@Component({
  selector: 'app-position-detail-modal',
  templateUrl: './position-detail-modal.component.html',
  styleUrls: ['./position-detail-modal.component.scss']
})
export class PositionDetailModalComponent implements OnInit {

  poste: any;
  competences: any[] = [];

  constructor(
    public dialogRef: MatDialogRef<PositionDetailModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private posteService: PosteService,
    private competenceService: CompetenceService
  ) { }

  ngOnInit(): void {
    const id = this.data.code; // Ensure you're using 'code' which is passed from the parent component
    this.getPoste(id);
    this.getCompetences(id);
  }

  getPoste(id: number): void {
    if (id) {
      this.posteService.getPosteById(id).subscribe(
        response => {
          this.poste = response;
          console.log('Poste:', this.poste);
        },
        error => {
          console.error('Error fetching poste', error);
        }
      );
    } else {
      console.error('Invalid ID:', id);
    }
  }

  getCompetences(id: number): void {
    if (id) {
      this.posteService.currentPosteCompetence(id).subscribe(
        response => {
          this.competences = response;
          console.log('Competences:', this.competences);
        },
        error => {
          console.error('Error fetching competences', error);
        }
      );
    } else {
      console.error('Invalid ID:', id);
    }
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

  mapEvaluationValue(evaluation: string): string {
    const evaluationMap: { [key: string]: string } = {
      'EXCELLENT': 'Excellent',
      'BON': 'Bon',
      'MOYEN': 'Moyen',
      'FAIBLE': 'Faible',
      '1 STAR': '1 Étoile',
      '2 STARS': '2 Étoiles',
      '3 STARS': '3 Étoiles',
      '4 STARS': '4 Étoiles'
      // Add more mappings as necessary
    };
    return evaluationMap[evaluation] || evaluation;
  }

  closePopup(): void {
    this.dialogRef.close();
  }

}
