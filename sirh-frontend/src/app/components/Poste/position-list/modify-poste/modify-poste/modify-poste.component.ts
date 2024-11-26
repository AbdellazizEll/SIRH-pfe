import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {PosteService} from "../../../../../_services/poste.service";
import {CompetenceService} from "../../../../../_services/competence.service";

@Component({
  selector: 'app-modify-poste',
  templateUrl: './modify-poste.component.html',
  styleUrls: ['./modify-poste.component.scss']
})
export class ModifyPosteComponent implements OnInit {
  posteForm!: FormGroup;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  availableCompetences: any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<ModifyPosteComponent>,
    private fb: FormBuilder,
    private posteService: PosteService,
    private competenceService:CompetenceService
  ) {
    this.posteForm = this.fb.group({
      titre: ['', Validators.required],
      requiredCompetence: this.fb.array([]),
      assignedCollaborators: this.fb.array([]) // Add FormArray for collaborators
// Initialize as form array for dynamic competencies
    });
  }

  ngOnInit(): void {
    this.setPosteData(this.data.poste); // Load poste data
  }

  // Getter for requiredCompetence FormArray
  get competenceArray(): FormArray {
    return this.posteForm.get('requiredCompetence') as FormArray;
  }

  get assignedCollaborators(): FormArray {
    return this.posteForm.get('assignedCollaborators') as FormArray;
  }

  // Populate the form with poste data and required competencies
  setPosteData(poste: any): void {
    this.posteForm.patchValue({ titre: poste.titre });

    // Set required competencies
    poste.requiredCompetence.forEach((competence: any) => {
      const controlGroup = this.fb.group({
        competenceId: [competence.competence.id],
        name: [{ value: competence.competence.name, disabled: true }],
        evaluation: [competence.evaluation, Validators.required],
        possibleValues: [competence.competence.possibleValues]
      });
      this.competenceArray.push(controlGroup);
    });

    // Set assigned collaborators
    poste.assignedCollaborators.forEach((collaborator: any) => {
      const collabGroup = this.fb.group({
        id: [collaborator.id],
        firstname: [{ value: collaborator.firstname, disabled: true }],
        lastname: [{ value: collaborator.lastname, disabled: true }],
        email: [{ value: collaborator.email, disabled: true }]
      });
      this.assignedCollaborators.push(collabGroup);
    });
  }

  removeCollaborator(index: number): void {
    // const removedCollaboratorId = this.assignedCollaborators.at(index).get('id')?.value;
    //
    // this.posteService.unassignCollaboratorFromPoste(removedCollaboratorId, this.data.poste.idPoste).subscribe(
    //   () => {
    //     this.successMessage = 'Collaborateur retiré avec succès';
    //     this.assignedCollaborators.removeAt(index); // Remove from FormArray
    //   },
    //   error => {
    //     this.errorMessage = 'Erreur lors de la suppression du collaborateur';
    //     console.error('Error removing collaborator:', error);
    //   }
    // );
  }
  // Handle form submission
  onSubmit(): void {
    // if (this.posteForm.valid) {
    //   const formData = {
    //     titre: this.posteForm.get('titre')?.value,
    //     requiredCompetence: this.competenceArray.controls.map(control => ({
    //       competenceId: control.get('competenceId')?.value,
    //       evaluation: control.get('evaluation')?.value
    //     }))
    //   };
      const updatedPost ={
        titre: this.posteForm.get('titre')?.value
      };

      this.posteService.updatePoste(this.data.poste.idPoste,updatedPost).subscribe(
        (response) => {
          this.successMessage = 'Les informations du poste ont été modifié avec succés';
         // this.hideMessageAfterDelay();
          //this.reloadPage();


          // update competence Evaluations
          const competencesArray = this.competenceArray;
          competencesArray.controls.forEach(control => {
            const competneceId = control.get('competenceId').value;
            const evaluation = control.get('evaluation').value;
            this.competenceService.updateCompetenceEvaluationPoste(this.data.poste.idPoste,competneceId,evaluation).subscribe(
              ()=> {
                console.log(`Competence ID : ${competneceId} updated Successuflly`);
              },
              error => {
                console.error(`Error updating competence ID :  ${competneceId}`,error)
              }
            );

          });

          }, error => {
          this.errorMessage ='Error updating poste data , Please try again ';
        //  this.hideMessageAfterDelay(); // Hide error message after delay
        }
      )

      // // Step 1: Update the Poste (title)
      // this.posteService.updatePoste(this.data.poste.idPoste, formData).subscribe(
      //   response => {
      //     // Step 2: Now, update each competence's evaluation
      //     const competenceUpdates = formData.requiredCompetence.map((competence) => {
      //       return this.competenceService.updateCompetenceEvaluationPoste(
      //         this.data.poste.idPoste,  // Poste ID
      //         competence.competenceId,  // Competence ID
      //         competence.evaluation     // New evaluation value
      //       ).toPromise(); // Convert each Observable to a promise
      //     });
      //
      //     // Step 3: Wait for all the competence evaluations to be updated
      //     Promise.all(competenceUpdates)
      //       .then(() => {
      //         this.successMessage = 'Poste and competencies updated successfully!';
      //         this.dialogRef.close(response);
      //       })
      //       .catch(error => {
      //         this.errorMessage = 'Error updating competence evaluations.';
      //         console.error('Error updating evaluations:', error);
      //       });
      //   },
      //   error => {
      //     this.errorMessage = 'Error updating poste.';
      //     console.error('Error updating poste:', error);
      //   }
      // );
    }

  private hideMessageAfterDelay() {
    setTimeout(() => {
      this.successMessage = null;
      this.errorMessage = null;
    }, 3000);
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


  closepopup(): void {
    this.dialogRef.close();
  }

  private reloadPage() {
    window.location.reload();

  }
}
