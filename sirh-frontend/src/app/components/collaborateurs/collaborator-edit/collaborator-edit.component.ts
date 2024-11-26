import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CollaboratorService } from "../../../_services/collaborator.service";
import { TokenStorageService } from "../../../_services/token-storage.service";
import { CompetenceService } from "../../../_services/competence.service";

@Component({
  selector: 'app-collaborator-edit',
  templateUrl: './collaborator-edit.component.html',
  styleUrls: ['./collaborator-edit.component.scss']
})
export class CollaboratorEditComponent implements OnInit {
  collaboratorId: number;
  editdata: any;
  myform: FormGroup;
  roles: any = [];
  rolesRemoval: any = [];
  selectedRoleAssign: any;
  selectedRoleRemoval: any;
  availableCompetences: any;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  // Role name mapping
  private roleNameMapping: { [key: string]: string } = {
    'ROLE_RH': 'Ressources Humaines',
    'ROLE_MANAGER': 'Manager',
    'ROLE_COLLABORATOR': 'Collaborateur',
    // Add other roles as needed
  };

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private collaboratorService: CollaboratorService,
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
      competences: this.fb.array([]) // FormArray for competences
    });
  }

  ngOnInit(): void {
    this.collaboratorId = this.route.snapshot.params['id']; // Fetch collaborator ID from route

    // Set the collaborator data
    this.setCollaboratorData(this.collaboratorId);
    this.fetchRoles();
    this.availableRoles();
    this.fetchAvailableCompetences();
  }

  get competencesArray(): FormArray {
    return this.myform.get('competences') as FormArray;
  }

  private setCollaboratorData(id: number): void {
    this.collaboratorService.findById(id).subscribe(
      (response: any) => {
        this.editdata = response;
        this.myform.patchValue({
          firstname: this.editdata.firstname,
          lastname: this.editdata.lastname,
          email: this.editdata.email,
          phone: this.editdata.phone
        });
        this.roles = response.roles || [];
        this.rolesRemoval = response.rolesRemoval || [];
      },
      (error: any) => {
        console.error('Error fetching collaborator data: ', error);
      }
    );
  }

  fetchRoles(): void {
    this.collaboratorService.fetchUserRoles(this.collaboratorId).subscribe(
      (data: any) => {
        this.roles = data.map((role: any) => ({
          id: role.id,
          value: role.name,
          displayName: this.mapRoleName(role.name)
        }));
      },
      (error: any) => {
        console.error('Error fetching roles: ', error);
      }
    );
  }

  availableRoles(): void {
    this.collaboratorService.availableRoles(this.collaboratorId).subscribe(
      (data: any) => {
        this.rolesRemoval = data.map((role: any) => ({
          id: role.id,
          value: role.name,
          displayName: this.mapRoleName(role.name)
        }));
      },
      (error: any) => {
        console.error('Error fetching available roles: ', error);
      }
    );
  }

  fetchAvailableCompetences(): void {
    this.competenceService.CurrentUserCompetence(this.collaboratorId).subscribe(
      (response: any) => {
        this.availableCompetences = response;
        this.initializeCompetenceControls();
      },
      (error: any) => {
        console.error('Error fetching available competences: ', error);
      }
    );
  }

  initializeCompetenceControls(): void {
    const competencesArray = this.competencesArray;

    this.availableCompetences.forEach((competence: any) => {
      const controlGroup = this.fb.group({
        competenceId: [competence.competence.id], // competenceId for reference
        name: [{ value: this.mapCompetenceName(competence.competence.name), disabled: true }], // name
        evaluation: [competence.evaluation, Validators.required],
        possibleValues: [competence.competence.possibleValues] // Values for the dropdown
      });
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

    this.collaboratorService.updateUser(this.collaboratorId, updatedCollaborator).subscribe(
      (response: any) => {
        this.successMessage = 'Collaborateur mis à jour avec succès!';
        this.hideMessageAfterDelay();

        // Handle role assignment and removal
        if (this.selectedRoleAssign) {
          this.collaboratorService.assignRole(this.collaboratorId, this.selectedRoleAssign.id).subscribe();
        }
        if (this.selectedRoleRemoval) {
          this.collaboratorService.removeRole(this.collaboratorId, this.selectedRoleRemoval.id).subscribe();
        }

        // Update competence evaluations
        const competencesArray = this.competencesArray;
        competencesArray.controls.forEach(control => {
          const competenceId = control.get('competenceId').value;
          const evaluation = control.get('evaluation').value;
          this.competenceService.updateCollaboratorEvaluation(this.collaboratorId, competenceId, evaluation).subscribe();
        });
      },
      (error: any) => {
        this.errorMessage = 'Erreur lors de la mise à jour du collaborateur.';
        this.hideMessageAfterDelay();
      }
    );
  }

  hideMessageAfterDelay(): void {
    setTimeout(() => {
      this.successMessage = null;
      this.errorMessage = null;
    }, 3000);
  }

  onRoleSelectionChange(event: any): void {
    this.selectedRoleAssign = event.value;
  }

  onRoleSelectionChange2(event: any): void {
    this.selectedRoleRemoval = event.value;
  }

  mapRoleName(name: string): string {
    return this.roleNameMapping[name] || name;
  }

  mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      'English': 'Anglais',
      'French': 'Français',
      'JAVA': 'Java',
      'Soft Skills': 'Compétences Douces'
    };
    return competenceMap[name] || name;
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
}
