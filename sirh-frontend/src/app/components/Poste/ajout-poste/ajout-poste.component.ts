import {Component, Inject, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CompetenceService} from "../../../_services/competence.service";
import {PosteService} from "../../../_services/poste.service";

@Component({
  selector: 'app-ajout-poste',
  templateUrl: './ajout-poste.component.html',
  styleUrls: ['./ajout-poste.component.scss']
})
export class AjoutPosteComponent implements OnInit {

  positionForm: FormGroup;
  availableCompetences: any = [];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AjoutPosteComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private competenceService: CompetenceService,
    private positionService: PosteService
  ) {
    this.positionForm = this.fb.group({
      titre: ['', Validators.required],
      competences: this.fb.array([])
    });
  }

  ngOnInit(): void {
    const id = this.data.id;

    this.fetchAvailableCompetences();
  }

  get competencesArray(): FormArray {
    return this.positionForm.get('competences') as FormArray;
  }

  fetchAvailableCompetences() {
    this.competenceService.getAllCompetences().subscribe(

      (response) => {
        this.availableCompetences = response;
        console.log(this.availableCompetences);
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
    const selectedCompetence = this.availableCompetences.find((comp:any) => comp.id === competenceId);
    if (selectedCompetence) {
      this.competencesArray.at(index).get('possibleValues').setValue(selectedCompetence.possibleValues);
    }
  }

  onSubmit(): void {
    if (this.positionForm.valid) {
      const titre = this.positionForm.get('titre').value;
      const competences = this.positionForm.get('competences').value;

      // First, create the position
      this.positionService.createPoste({ titre: titre }).subscribe(
        (response: any) => {
          const posteId = response.idPoste; // Assuming the response contains the id of the created position
          this.assignCompetencesToPoste(posteId, competences);
        },
        (error: any) => {
          console.error('Error creating position:', error);
        }
      );
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  private assignCompetencesToPoste(posteId: any, competences: any) {
    const requests = competences.map((competence:any)=> {
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

}
