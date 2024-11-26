import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { DepartmentService } from "../../../_services/department.service";
import { CollaboratorService } from "../../../_services/collaborator.service";

@Component({
  selector: 'app-modifier-departement',
  templateUrl: './modifier-departement.component.html',
  styleUrls: ['./modifier-departement.component.scss']
})
export class ModifierDepartementComponent implements OnInit {
  updateDepartmentForm: FormGroup;
  setResponsibleForm: FormGroup;
  responsibles: any[] = [];
  successMessage: string;
  errorMessage: string;

  constructor(
    private fb: FormBuilder,
    private departmentService: DepartmentService,
    public dialogRef: MatDialogRef<ModifierDepartementComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private collaboratorService: CollaboratorService
  ) {}

  ngOnInit(): void {
    this.updateDepartmentForm = this.fb.group({
      nomDep: [this.data.department.nomDep, Validators.required],
    });

    this.setResponsibleForm = this.fb.group({
      responsable: [this.data.department.responsable ? this.data.department.responsable.id : '', Validators.required]
    });

    this.loadResponsibles();
  }

  private loadResponsibles() {
    this.collaboratorService.fetchResponsiblesAvailable().subscribe(
      response => {
        this.responsibles = response;
      },
      error => {
        console.error('Error loading managers:', error);
      }
    );
  }

  updateDepartmentDetails() {
    if (this.updateDepartmentForm.valid) {
      const updatedDetails = {
        nomDep: this.updateDepartmentForm.value.nomDep
      };
      this.departmentService.updateDepartmentDetails(this.data.department.id_dep, updatedDetails).subscribe(
        response => {
          this.successMessage = 'Department name updated successfully!';
          this.data.department.nomDep = updatedDetails.nomDep;
          setTimeout(() => (this.successMessage = ''), 3000);
        },
        error => {
          this.errorMessage = 'Failed to update department name.';
          setTimeout(() => (this.errorMessage = ''), 3000);
        }
      );
    }
  }

  setResponsiblePerson() {
    if (this.setResponsibleForm.valid) {
      const newResponsibleId = this.setResponsibleForm.value.responsable;
      const currentResponsibleId = this.data.department.responsable ? this.data.department.responsable.id : null;

      if (currentResponsibleId && currentResponsibleId !== newResponsibleId) {
        // Unassign the current responsible before setting the new one
        this.departmentService.unAssignResponsible(this.data.department.id_dep, currentResponsibleId).subscribe(
          () => {
            this.assignNewResponsible(newResponsibleId);
          },
          error => {
            this.errorMessage = 'Failed to unassign current responsible.';
            setTimeout(() => (this.errorMessage = ''), 3000);
          }
        );
      } else {
        // Directly assign if there is no current responsible or if the responsible hasn't changed
        this.assignNewResponsible(newResponsibleId);
      }
    }
  }

  private assignNewResponsible(collaboratorId: number) {
    this.departmentService.assignManagerToDepartment(this.data.department.id_dep, collaboratorId).subscribe(
      response => {
        this.successMessage = 'Responsible person set successfully!';
        const selectedResponsible = this.responsibles.find(r => r.id === collaboratorId);
        this.data.department.responsable = selectedResponsible;
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error => {
        this.errorMessage = 'Failed to set responsible person.';
        setTimeout(() => (this.errorMessage = ''), 3000);
      }
    );
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
