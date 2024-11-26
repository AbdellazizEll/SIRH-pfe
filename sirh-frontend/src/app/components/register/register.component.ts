import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { AuthService } from "../../_services/auth.service";
import { CollaboratorService } from "../../_services/collaborator.service";
import { EquipesService } from "../../_services/equipes.service";
import { Router } from "@angular/router";

type RoleName = 'RH' | 'MANAGER' | 'COLLABORATOR';

const roleNameMapping: Record<RoleName, string> = {
  'RH': 'Gestionnaire RH',
  'MANAGER': 'Manager',
  'COLLABORATOR': 'Collaborateur'
};

@Component({
  selector: 'app-register',
  templateUrl: 'register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  roles: { id: number; name: string; displayName: string }[] = [];
  equipes: { id_equipe: number; nom: string }[] = [];
  successMessage: string = '';
  errorMessage: string = '';
  isManagerRoleSelected: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private collaboratorService: CollaboratorService,
    private equipeServ: EquipesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      firstname: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      phone: ['', [Validators.required, Validators.pattern('^\\+216[0-9]{8}$')]],
      role: ['', [Validators.required]],
      equipe: [''],
      managerType: ['']  // Add managerType here without a validator initially
    }, {
      validator: this.passwordMatchValidator
    });

    this.fetchRoles();
    this.fetchEquipes();
  }

  // Fetch roles
  fetchRoles() {
    this.collaboratorService.getAllRoles().subscribe(
      (response) => {
        this.roles = response.data.List.map((role: { id: number; name: string }) => ({
          id: role.id,
          name: role.name.replace('ROLE_', ''),
          displayName: roleNameMapping[role.name.replace('ROLE_', '') as RoleName]
        }));
      },
      (error) => {
        console.error('Error fetching roles:', error);
      }
    );
  }

  // Fetch equipes
  fetchEquipes() {
    this.equipeServ.getEquipes().subscribe(
      (response) => {
        if (response?.data?.page?.content) {
          this.equipes = response.data.page.content.map((equipe: { id_equipe: number; nom: string }) => ({
            id_equipe: equipe.id_equipe,
            nom: equipe.nom
          }));
        } else {
          console.error('Unexpected API structure or empty content:', response);
          this.equipes = [];
        }
      },
      (error) => {
        console.error('Error fetching equipes:', error);
      }
    );
  }

  // Check if password and confirmPassword match
  passwordMatchValidator(control: AbstractControl) {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    if (password !== confirmPassword) {
      control.get('confirmPassword')?.setErrors({ passwordMismatch: true });
    } else {
      control.get('confirmPassword')?.setErrors(null);
    }
  }

  // Handle role change to show/hide managerType
  onRoleChange(event: any) {
    const selectedRole = event.target.value;
    this.isManagerRoleSelected = selectedRole === 'MANAGER';

    // Update the managerType control's validation based on role selection
    if (this.isManagerRoleSelected) {
      this.registerForm.get('managerType')?.setValidators([Validators.required]);
    } else {
      this.registerForm.get('managerType')?.clearValidators();
    }
    this.registerForm.get('managerType')?.updateValueAndValidity();
  }

  // Submit form
  submitForm() {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const formValue = this.registerForm.value;

    // If no team is selected, set equipe to null
    if (!formValue.equipe) {
      formValue.equipe = null;
    }

    // Prepare the role and managerType
    const role = formValue.role.toUpperCase();
    const managerType = this.isManagerRoleSelected ? formValue.managerType : null;

    const requestBody = {
      ...formValue,
      role: [role],
      managerType  // Include managerType if selected
    };

    this.authService.register(requestBody).subscribe(
      (response) => {
        this.successMessage = "Collaborateur a été ajouté avec succès";
        this.errorMessage = '';
        const collaboratorId = response.id;
        const equipeId = formValue.equipe;

        if (equipeId) {
          this.equipeServ.assignToEquipe(collaboratorId, equipeId).subscribe(
            (error) => console.error('Error assigning collaborator to equipe:', error)
          );
        }
      },
      (error) => {
        this.errorMessage = "Ajout de collaborateur a échoué";
        this.successMessage = '';
        console.error('Registration error:', error);
      }
    );
  }

  navigateHome(): void {
    this.router.navigate(['/collaborateurs']);
  }
}
