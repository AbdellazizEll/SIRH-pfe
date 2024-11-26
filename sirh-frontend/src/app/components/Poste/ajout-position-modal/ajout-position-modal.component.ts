import {Component, Inject, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AjoutPosteComponent} from "../ajout-poste/ajout-poste.component";
import {CompetenceService} from "../../../_services/competence.service";
import {PosteService} from "../../../_services/poste.service";

@Component({
  selector: 'app-ajout-position-modal',
  templateUrl: './ajout-position-modal.component.html',
  styleUrls: ['./ajout-position-modal.component.scss']
})
export class AjoutPositionModalComponent implements OnInit {

  positionForm: FormGroup;
  availableCompetences: any[] = [];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AjoutPositionModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private competenceService: CompetenceService,
    private positionService: PosteService
  ) {
    this.positionForm = this.fb.group({
      titre: ['', Validators.required],
      competences: this.fb.array([]),
    });
  }

  ngOnInit(): void {
    this.fetchAvailableCompetences();
  }

  get competencesArray(): FormArray {
    return this.positionForm.get('competences') as FormArray;
  }

  fetchAvailableCompetences() {
    this.competenceService.getAllCompetences().subscribe(
      (response) => {
        // Extract competences from the response
        this.availableCompetences = response.data.page.content;
        console.log("Available Competences:", this.availableCompetences);
      },
      (error: any) => {
        console.error('Error fetching competences:', error);
      }
    );
  }

  addCompetence(): void {
    const competenceGroup = this.fb.group({
      competenceId: ['', Validators.required],
      evaluation: ['', Validators.required],
      possibleValues: [[]]
    });
    this.competencesArray.push(competenceGroup);
  }

  removeCompetence(index: number): void {
    this.competencesArray.removeAt(index);
  }

  onCompetenceChange(index: number): void {
    const competenceId = this.competencesArray.at(index).get('competenceId').value;
    const selectedCompetence = this.availableCompetences.find((comp: any) => comp.id === competenceId);

    if (selectedCompetence) {
      this.competencesArray.at(index).get('possibleValues').setValue(selectedCompetence.possibleValues);
    }
  }

  onSubmit(): void {
    if (this.positionForm.valid) {
      const titre = this.positionForm.get('titre').value;
      const competences = this.positionForm.get('competences').value;

      this.positionService.createPoste({ titre: titre }).subscribe(
        (response: any) => {
          const posteId = response.idPoste;
          this.assignCompetencesToPoste(posteId, competences);
        },
        (error: any) => {
          console.error('Error creating position:', error);
        }
      );
    }
  }

  mapEvaluationValue(value: string): string {
    const evaluationMap: { [key: string]: string } = {
      '1 STAR': '1 Étoile',
      '2 STARS': '2 Étoiles',
      '3 STARS': '3 Étoiles',
      '4 STARS': '4 Étoiles',
      'EXCELLENT': 'Excellent',
      'BON': 'Bon',
      'MOYEN': 'Moyen',
      'FAIBLE': 'Faible'
    };
    return evaluationMap[value] || value;
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  private assignCompetencesToPoste(posteId: any, competences: any) {
    const requests = competences.map((competence: any) => {
      return this.positionService.addCompetenceToPoste(posteId, competence.competenceId, competence.evaluation).toPromise();
    });
    Promise.all(requests).then(
      () => {
        this.dialogRef.close(true);
        console.log('All competences assigned successfully');
      },
      (error) => {
        console.error('Error assigning competences:', error);
      }
    );
  }

  mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      'Anglais': 'Anglais',
      'Français': 'Français',
      'JAVA': 'Java',
      'Soft Skills': 'Compétences Douces'
    };
    return competenceMap[name] || name;
  }

}
