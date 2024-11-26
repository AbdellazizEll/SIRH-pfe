import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../_services/auth.service";
import { CollaboratorService } from "../../_services/collaborator.service";
import { DepartmentService } from "../../_services/department.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-departement',
  templateUrl: './departement.component.html',
  styleUrls: ['./departement.component.scss']
})
export class DepartementComponent implements OnInit {
  departmentForm!: FormGroup;
  responsibles: { id: number; email: string }[] = [];
  errorMessage: String;
  successMessage: String;

  constructor(
    private departmentService: DepartmentService,
    private collaboratorService: CollaboratorService,
    private fb: FormBuilder,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.departmentForm = this.fb.group({
      nomDep: ['', [Validators.required]],
      responsible: [''] // Remove Validators.required to make it optional
    });

    this.loadResponsibles();
  }

// DepartementComponent
  submitForm() {
    const formValue = this.departmentForm.value;

    const departmentPayload = {
      nomDep: formValue.nomDep
    };

    this.departmentService.addDepartment(departmentPayload).subscribe(
      (response) => {
        console.log('Department added successfully:', response);
        this.successMessage = "Ajout Département a été effectué avec succès";
        this.errorMessage = '';

        const departmentId = response.id_dep; // Assuming the response contains the new department's ID

        if (formValue.responsible) {
          this.departmentService.assignManagerToDepartment(departmentId, formValue.responsible).subscribe(
            () => {
              console.log('Manager assigned to department successfully');
              this.successMessage += " et le responsable a été attribué.";
              setTimeout(() => this.reloadPage(), 4000); // Delay to display success message before reloading
            },
            (error) => {
              console.error('Error assigning manager to department:', error);
              this.errorMessage = error.error.message || 'Error assigning manager to department';
              this.successMessage = '';
              setTimeout(() => this.errorMessage = '', 4000);
            }
          );

        } else {
          setTimeout(() => this.reloadPage(), 4000);
        }
        this.redirectPage();
      },
      (error) => {
        console.error('Error adding department:', error);
        this.errorMessage = error.error.message || 'An unexpected error occurred while adding the department.';
        setTimeout(() => this.errorMessage = '', 4000);
      }
    );
  }

  private loadResponsibles() {
    this.collaboratorService.fetchResponsiblesAvailable() .subscribe(
      (response) => {
        console.log(response);

          this.responsibles = response.map((responsible: { id: number; email: string }) => ({
            id: responsible.id,
            email: responsible.email,
          }));

      },
      (error) => {
        console.error('Error loading managers:', error);
      }
    );
  }

  private reloadPage() {
    window.location.reload();
  }

  private redirectPage(){
    this.router.navigate(['GererDepartement'])
  }

  onCancel() {

  }
}
