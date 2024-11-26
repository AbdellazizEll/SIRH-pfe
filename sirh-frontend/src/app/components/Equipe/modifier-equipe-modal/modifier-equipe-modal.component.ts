import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { EquipesService } from "../../../_services/equipes.service";
import { CollaboratorService } from "../../../_services/collaborator.service";
import { DepartmentService } from "../../../_services/department.service";

@Component({
  selector: 'app-modifier-equipe-modal',
  templateUrl: './modifier-equipe-modal.component.html',
  styleUrls: ['./modifier-equipe-modal.component.scss']
})
export class ModifierEquipeModalComponent implements OnInit {

  teamForm: FormGroup;
  availableManagers: any[] = [];
  availableDepartments: any[] = [];
  successMessage: string = '';
  errorMessage: string = '';
  isEditMode = false;
  action: string = ''; // Track the current action for displaying messages

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ModifierEquipeModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private equipeService: EquipesService,
    private collaboratorService: CollaboratorService,
    private departmentService: DepartmentService
  ) {}

  ngOnInit(): void {
    this.isEditMode = !!this.data.id;

    this.teamForm = this.fb.group({
      nom: ['', Validators.required],
      manager: ['', Validators.required],
      department: ['', Validators.required]
    });

    this.fetchAvailableManagers();
    this.fetchAvailableDepartments();
    this.getTeamDetail();
  }

  private fetchAvailableManagers() {
    this.collaboratorService.fetchManagerEquipe().subscribe(
      data => {
        this.availableManagers = data;
      },
      error => {
        this.action = 'assignManager';
        this.errorMessage = 'Error fetching managers.';
      }
    );
  }

  private fetchAvailableDepartments() {
    this.departmentService.getDepartments().subscribe(
      response => {
        this.availableDepartments = response.content || [];
      },
      error => {
        this.action = 'assignDepartment';
        this.errorMessage = 'Error fetching departments.';
      }
    );
  }

  private getTeamDetail() {
    if (this.isEditMode) {
      this.equipeService.getEquipeById(this.data.id).subscribe(
        data => {
          this.teamForm.patchValue({
            nom: data.nom,
            manager: data.managerEquipe?.id || '',
            department: data.fromDepartment?.id_dep || ''
          });
        },
        error => {
          this.errorMessage = 'Error loading team details.';
        }
      );
    }
  }

  onUpdateTeam(): void {
    this.clearMessages();
    this.action = 'updateTeam';

    if (this.teamForm.get('nom')?.valid) {
      const teamData = { nom: this.teamForm.get('nom')?.value };

      this.equipeService.updateTeam(this.data.id, teamData).subscribe(
        response => {
          this.successMessage = "Modification a été effectué avec succés";
        },
        error => {
          this.errorMessage = 'Erreur';
        }
      );
    } else {
      this.errorMessage = 'Please enter a valid team name.';
    }
  }

  onAssignManager(): void {
    this.clearMessages();
    this.action = 'assignManager';

    if (this.teamForm.get('manager')?.valid) {
      const managerId = this.teamForm.get('manager')?.value;

      this.equipeService.assignManagerToEquipe(managerId, this.data.id).subscribe(
        response => {
          this.successMessage = 'Manager a été effectué avec succés ';

        },
        error => {
          this.errorMessage = "Erreur lors d'affectation manager";
        }
      );
    } else {
      this.errorMessage = 'Please select a valid manager.';
    }
  }

  onAssignDepartment(): void {
    this.clearMessages();
    this.action = 'assignDepartment';

    if (this.teamForm.get('department')?.valid) {
      const departmentId = this.teamForm.get('department')?.value;

      this.equipeService.assignEquipeDepartment(this.data.id, departmentId).subscribe(
        response => {
          this.successMessage = 'Departement a été effectué avec succés ';
          this.reloadPage();
        },
        error => {
          this.errorMessage = "Erreur";
        }
      );
    } else {
      this.errorMessage = 'Please select a valid department.';
    }
  }

  clearMessages() {
    this.successMessage = '';
    this.errorMessage = '';
  }

  close(): void {
    this.dialogRef.close();
  }

  reloadPage(){
    window.location.reload();
  }
}
