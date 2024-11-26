import { Component, Input, OnInit } from '@angular/core';
import { DepartmentService } from "../../../_services/department.service";
import { ActivatedRoute, Router } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { EquipesService } from "../../../_services/equipes.service";
import { CollaboratorService } from "../../../_services/collaborator.service";
import { AddTeamModalComponent } from "../add-team-modal/add-team-modal.component";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ProfilCollabComponent } from "../../Equipe/profil-collab/profil-collab.component";
import { auto } from "@popperjs/core";
import { MatDialog } from "@angular/material/dialog";

@Component({
  selector: 'app-profil-departement',
  templateUrl: './profil-departement.component.html',
  styleUrls: ['./profil-departement.component.scss']
})
export class ProfilDepartementComponent implements OnInit {

  departmentId: any;
  department: any;
  responsibles: { id: number; email: string }[] = [];
  errorMessage: string;
  successMessage: string;
  updateDepartmentForm: FormGroup;

  // Professional avatars paths
  professionalAvatars: string[] = [
    './assets/Avatars/asset1.jpg',
    './assets/Avatars/asset2.jpg',
    './assets/Avatars/asset3.jpg',
    './assets/Avatars/asset4.jpg',
    './assets/Avatars/asset5.jpg',
    './assets/Avatars/asset6.jpg',
  ];

  // Team-specific avatars
  teamAvatars: string[] = [
    './assets/Avatars/team.jpg',
    './assets/Avatars/team2.jpg',
    './assets/Avatars/team3.jpg',
  ];

  // Avatar URLs for responsible and teams
  responsableAvatarUrl: string;
  teamAvatarUrls: { [id: number]: string } = {};

  constructor(private departmentService: DepartmentService,
              private modalService: NgbModal,
              private router: Router,
              private route: ActivatedRoute,
              private equipeService: EquipesService,
              private collaboratorService: CollaboratorService,
              private fb: FormBuilder,
              private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.updateDepartmentForm = this.fb.group({
      nomDep: ['', Validators.required],
      responsable: ['']
    });

    this.route.params.subscribe(params => {
      this.departmentId = +params['id'];
      this.getDepartmentDetails();
      this.loadResponsibles();
    });
  }

  getDepartmentDetails() {
    this.departmentService.getDepartmentById(this.departmentId).subscribe(data => {
      this.department = data;
      console.log('Loaded Department:', this.department);
      this.updateDepartmentForm.patchValue({
        nomDep: this.department.nomDep,
        responsable: this.department.responsable?.id || ''
      });

      // Compute avatars for the department responsible and teams
      this.computeAvatarUrls();
    }, error => {
      console.error('Error fetching department details', error);
    });
  }

  private loadResponsibles() {
    this.collaboratorService.fetchResponsiblesAvailable().subscribe(
      response => {
        this.responsibles = response.responsable;
        console.log(this.responsibles);
      },
      error => {
        console.error('Error loading managers:', error);
      }
    );
  }

  updateDepartment() {
    if (this.updateDepartmentForm.valid) {
      const updatedDepartment = {
        id_dep: this.department.id_dep,
        nomDep: this.updateDepartmentForm.value.nomDep,
        responsable: {
          id: this.updateDepartmentForm.value.responsable
        }
      };

      console.log('Form Value:', updatedDepartment);
      console.log('Department responsible:', updatedDepartment.responsable);

      if (updatedDepartment.id_dep) {
        this.departmentService.updateDepartmentDetails(updatedDepartment.id_dep, updatedDepartment).subscribe(
          () => {
            console.log('Department updated successfully', updatedDepartment);
            this.successMessage = "Mise à jour du département effectuée avec succès";
            this.errorMessage = '';
            setTimeout(() => this.successMessage = '', 4000);
            this.reloadDepartmentDetails();
          },
          error => {
            console.error('Error updating department:', error);
            this.successMessage = '';
            this.errorMessage = "Échec de la mise à jour du département";
            setTimeout(() => this.errorMessage = '', 4000);
          }
        );
      }
    }
  }

  private computeAvatarUrls() {
    // Assign avatar to the department responsible
    if (this.department?.responsable) {
      const index = this.department.responsable.id % this.professionalAvatars.length;
      this.responsableAvatarUrl = this.professionalAvatars[index];
    } else {
      // Default avatar if no responsible is assigned
      this.responsableAvatarUrl = './assets/Avatars/default.png';
    }

    // Assign avatars to the teams using team-specific images
    if (this.department?.equipes) {
      for (let team of this.department.equipes) {
        const index = team.id_equipe % this.teamAvatars.length;
        this.teamAvatarUrls[team.id_equipe] = this.teamAvatars[index];
      }
    }
  }

  openAddTeamModal() {
    const modalRef = this.modalService.open(AddTeamModalComponent);
    modalRef.componentInstance.departmentId = this.departmentId;
    modalRef.componentInstance.departmentName = this.department.nomDep;
    modalRef.result.then((result) => {
      if (result === 'success') {
        this.successMessage = "Ajout d'équipe effectué";
        this.errorMessage = '';
        setTimeout(() => this.successMessage = '', 4000);
        this.reloadDepartmentDetails();
      }
    }, (reason) => {
      this.successMessage = '';
      this.errorMessage = "Échec de l'ajout de l'équipe";
      setTimeout(() => this.errorMessage = '', 4000);
      console.log('Dismissed');
    });
  }

  openCollabDetail(member: any) {
    const dialogRef = this.dialog.open(ProfilCollabComponent, {
      width: auto,
      data: {
        collaborator: member
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'updated') {
        this.reloadDepartmentDetails();
      }
    });
  }

  reloadDepartmentDetails() {
    this.departmentService.getDepartmentById(this.department.id_dep).subscribe(
      response => {
        this.department = response;
        this.updateDepartmentForm.patchValue({
          nomDep: this.department.nomDep,
          responsable: this.department.responsable?.id || ''
        });

        // Recompute avatars after reloading
        this.computeAvatarUrls();
      },
      err => {
        console.error('Error reloading department details:', err);
      }
    );
  }

  openCollabDetailPage(id: number) {
    this.router.navigate(['collaborateurCompetenceDetail', id]);
  }

  openTeamProfile(teamId: number) {
    this.router.navigate(['teamProfileCard', teamId]);
  }

  redirectToDepartmentManagement() {
    this.router.navigate(['/GererDepartement']);
  }

  private reloadPage() {
    window.location.reload();
  }
}
