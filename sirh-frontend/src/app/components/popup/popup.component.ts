import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CollaboratorService } from '../../_services/collaborator.service';
import { TokenStorageService } from '../../_services/token-storage.service';
import { CompetenceService } from '../../_services/competence.service';

@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.scss']
})
export class PopupComponent implements OnInit {
  inputdata: any;
  editdata: any;
  myform: FormGroup;
  roles: any = [];
  rolesRemoval: any = [];
  selectedRoleAssign: any;
  selectedRoleRemoval: any;
  availableCompetences: any;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private ref: MatDialogRef<PopupComponent>,
    private fb: FormBuilder,
    private service: CollaboratorService,
    private storageService: TokenStorageService,
    private competenceService: CompetenceService
  ) {
    this.myform = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      roles: [null],
      rolesRm: [null],
      competences: this.fb.array([]) // Update to form array for dynamic competence controls
    });
  }

  ngOnInit(): void {
    this.inputdata = this.data;
    this.setpopupdata(this.inputdata.code);
    this.fetchRoles();
    this.availableRoles();
    this.fetchAvailableCompetences();
  }

  get competencesArray(): FormArray {
    return this.myform.get('competences') as FormArray;
  }

  closepopup(): void {
    this.ref.close();
  }

  private setpopupdata(code: number): void {
    this.service.findById(code).subscribe(
      response => {
        console.log('Response from server:', response);

        // Directly using the response as it contains the necessary data
        this.editdata = response;

        // Patch the form values
        this.myform.patchValue({
          firstname: this.editdata.firstname,
          lastname: this.editdata.lastname,
          email: this.editdata.email,
          phone: this.editdata.phone
        });

        this.roles = response.roles || [];
        this.rolesRemoval = response.rolesRemoval || [];

        console.log('Form values after patching:', this.myform.value);
      },
      error => {
        console.error('Error fetching collaborator data: ', error);
      }
    );
  }

  fetchRoles(): void {
    this.service.fetchUserRoles(this.inputdata.code).subscribe(
      data => {
        this.roles = data.map((role: any) => ({ id: role.id, value: role.name }));
      },
      error => {
        console.error('Error fetching roles: ', error);
      }
    );
  }

  availableRoles(): void {
    this.service.availableRoles(this.inputdata.code).subscribe(
      data => {
        this.rolesRemoval = data.map((role: any) => ({ id: role.id, value: role.name }));
      },
      error => {
        console.error('Error fetching available roles: ', error);
      }
    );
  }

  fetchAvailableCompetences(): void {
    this.competenceService.CurrentUserCompetence(this.inputdata.code).subscribe(
      response => {
        console.log(response)
        this.availableCompetences = response;
        this.initializeCompetenceControls(); // Initialize form controls for competences
      },
      error => {
        console.error('Error fetching available competences: ', error);
      }
    );
  }

  initializeCompetenceControls(): void {
    const competencesArray = this.competencesArray;

    this.availableCompetences.forEach((competence: any) => {
      const controlGroup = this.fb.group({
        competenceId: [competence.competence.id], // Bind competenceId for reference
        name: [{ value: this.mapCompetenceName(competence.competence.name), disabled: true }], // Display the name but disable it
        evaluation: [competence.evaluation, Validators.required],  // Bind the evaluation value here
        possibleValues: [competence.competence.possibleValues] // Provide possible values for the dropdown
      });

      // Push the control group for each competence into the form array
      competencesArray.push(controlGroup);
    });
  }
  updateCollaborator(): void {
    const updatedCollaborator = {
      firstname: this.myform.get('firstname').value,
      lastname: this.myform.get('lastname').value,
      email: this.myform.get('email').value,
      phone: this.myform.get('phone').value
    };

    this.service.updateUser(this.inputdata.code, updatedCollaborator).subscribe(
      response => {
        this.successMessage = 'Collaborator data updated successfully!';
        this.hideMessageAfterDelay();
        this.reloadPage()// Hide success message after delay
        if (this.selectedRoleAssign) {
          this.service.assignRole(this.inputdata.code, this.selectedRoleAssign.id).subscribe();
        }
        if (this.selectedRoleRemoval) {
          this.service.removeRole(this.inputdata.code, this.selectedRoleRemoval.id).subscribe();
        }

        // Update competence evaluations
        const competencesArray = this.competencesArray;
        competencesArray.controls.forEach(control => {
          const competenceId = control.get('competenceId').value;
          const evaluation = control.get('evaluation').value;
          this.competenceService.updateCollaboratorEvaluation(this.inputdata.code, competenceId, evaluation).subscribe(
            () => {
              console.log(`Competence ID: ${competenceId} updated successfully`);
            },
            error => {
              console.error(`Error updating competence ID: ${competenceId}`, error);
            }
          );
        });
      },
      error => {
        this.errorMessage = 'Error updating collaborator data. Please try again.';
        this.hideMessageAfterDelay(); // Hide error message after delay
      }
    );
  }

  hideMessageAfterDelay(): void {
    setTimeout(() => {
      this.successMessage = null;
      this.errorMessage = null;
    }, 3000); // Messages are hidden after 3 seconds
  }

  onRoleSelectionChange(event: any): void {
    this.selectedRoleAssign = event.value; // Full role object
  }

  onRoleSelectionChange2(event: any): void {
    this.selectedRoleRemoval = event.value; // Full role object
  }

  mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      'English': 'Anglais',
      'French': 'Français',
      'JAVA': 'Java',
      'Soft Skills': 'Compétences Douces'
      // Add more mappings as necessary
    };
    return competenceMap[name] || name; // Default to the original name if not found in the map
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

  reloadPage()
  {
    window.location.reload();
  }
}
