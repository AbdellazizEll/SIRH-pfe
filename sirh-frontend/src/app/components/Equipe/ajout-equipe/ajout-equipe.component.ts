import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { CollaboratorService } from "../../../_services/collaborator.service";
import { EquipesService } from "../../../_services/equipes.service";
import { DepartmentService } from "../../../_services/department.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-ajout-equipe',
  templateUrl: './ajout-equipe.component.html',
  styleUrls: ['./ajout-equipe.component.scss']
})
export class AjoutEquipeComponent implements OnInit {
  equipeForm!: FormGroup;
  managers: { id: number; email: string }[] = [];
  departement: { id: number; nom: string }[] = [];

  successMessage: String;
  errorMessage: String;

  constructor(
    private collaboratorService: CollaboratorService,
    private departmentService: DepartmentService,
    private equipeServ: EquipesService,
    private fb: FormBuilder,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.equipeForm = this.fb.group({
      nom: ['', [Validators.required]],
      fromDepartment: ['', [Validators.required]],
      managerEquipe: [null]
    });

    this.loadManagers();
    this.loadDepartments();
  }

  private loadManagers() {
    this.collaboratorService.fetchManagerEquipe().subscribe(
      (response) => {
          this.managers = response.map((manager: { id: number; email: string }) => ({
            id: manager.id,
            email: manager.email,
          }));

      },
      (error) => {
        console.error('Error loading managers:', error);
      }
    );
  }

  private loadDepartments() {
    this.departmentService.getDepartments().subscribe(
      (response) => {
        if (response.content) {
          this.departement = response.content.map((departement: { id_dep: number; nomDep: string }) => ({
            id: departement.id_dep,
            nom: departement.nomDep,
          }));
        } else {
          console.error('Unexpected response structure for departments:', response);
        }
      },
      (error) => {
        console.error('Error fetching departments:', error);
      }
    );
  }

  submitForm() {
    if (this.equipeForm.invalid) {
      return;
    }

    const formValue = this.equipeForm.value;

    const newTeam = {
      nom: formValue.nom,
      fromDepartment: { id_dep: formValue.fromDepartment },
      managerEquipe: formValue.managerEquipe ? { id: formValue.managerEquipe } : null
    };

    console.log('Submitting new team:', newTeam);

    this.equipeServ.addEquipe(newTeam).subscribe(
      (response) => {
        this.successMessage = "Ajout d'équipe a été effectué avec succés ";
        this.errorMessage = '';
        setTimeout(() => this.successMessage = '', 4000);

        this.reloadPage();
      },
      (error) => {
        this.successMessage = '';
        this.errorMessage = "Ajout d'équipe a echoué";
        setTimeout(() => this.errorMessage = '', 4000);
        console.error('Error creating team:', error);
        alert('Error creating team');
      }
    );
  }

  reloadPage(): void {
    window.location.reload();
  }

  onCancel() {
  this.router.navigate(["/GererEquipes"])
  }


}
