import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { EquipesService } from "../../../_services/equipes.service";
import { CollaboratorService } from "../../../_services/collaborator.service";

@Component({
  selector: 'app-add-team-modal',
  templateUrl: './add-team-modal.component.html',
  styleUrls: ['./add-team-modal.component.scss']
})
export class AddTeamModalComponent implements OnInit {

  @Input() departmentId: number;
  @Input() departmentName: string; // Display the department name for UI
  equipeForm!: FormGroup;
  successMessage = '';
  errorMessage = '';
  availableManagers: any = [];

  constructor(
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private equipeServ: EquipesService,
    private collaboratorService: CollaboratorService
  ) {}

  ngOnInit(): void {
    this.equipeForm = this.fb.group({
      nom: ['', [Validators.required]], // Team name is required
      manager: [''],  // Manager is optional now
      departement: [this.departmentId, [Validators.required]], // Department ID (hidden but required)
    });

    this.fetchAvailableManagers();
  }

  submitForm() {
    const formValue = this.equipeForm.value;

    // Prepare the new team object to match EquipeDTO structure
    const newTeam = {
      nom: formValue.nom,
      manager: formValue.manager || null, // Set to null if no manager selected
      departement: { id_dep: formValue.departement } // Ensure department ID structure matches backend
    };

    console.log('Submitting new team:', newTeam);

    // Call the service to create the new team
    this.equipeServ.addEquipeDepartment(newTeam, this.departmentId).subscribe(
      (response) => {
        this.successMessage = "L'équipe a été ajoutée avec succès";
        this.errorMessage = '';
        setTimeout(() => this.successMessage = '', 3000);
        console.log('Team created successfully:', response);
        this.activeModal.close('success');
      },
      (error) => {
        this.errorMessage = "Création d'équipe a échoué";
        console.error('Error creating team:', error);
      }
    );
  }

  private fetchAvailableManagers() {
    this.collaboratorService.fetchJoblessManagerEquipe().subscribe(
      data => {
        this.availableManagers = data;
        console.log(this.availableManagers)
      },
      error => {
        this.errorMessage = 'Error fetching managers.';
      }
    );
  }
}
