import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {EquipesService} from "../../../../../_services/equipes.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {CollaboratorService} from "../../../../../_services/collaborator.service";
import {AddCollaboratorsModalComponent} from "../../../../Equipe/add-collaborators-modal/add-collaborators-modal.component";
import {ProfilCollabComponent} from "../../../../Equipe/profil-collab/profil-collab.component";
import {auto} from "@popperjs/core";
import {ModifierEquipeModalComponent} from "../../../../Equipe/modifier-equipe-modal/modifier-equipe-modal.component";
import {TokenStorageService} from "../../../../../_services/token-storage.service";
import {ProfilCollabManagerComponent} from "../../../../Equipe/manager-equipe/ProfilCollabManager/profil-collab-manager/profil-collab-manager.component";

@Component({
  selector: 'app-team-profile',
  templateUrl: './team-profile.component.html',
  styleUrls: ['./team-profile.component.scss']
})
export class TeamProfileComponent implements OnInit {

  teamId: number;
  @Input() team: any;
  teamForm: FormGroup;
  errorMessage: string;
  successMessage: string;
  availableManagers: any[] = [];
  managerGender: string;
  memberGenders: { [id: number]: string } = {};
  origin: any;
  user: any;

  // Professional avatars paths
  professionalAvatars: string[] = [
    '/assets/Avatars/asset1.jpg',
    '/assets/Avatars/asset2.jpg',
    '/assets/Avatars/asset3.jpg',
    '/assets/Avatars/asset4.jpg',
    '/assets/Avatars/asset5.jpg',
    '/assets/Avatars/asset6.jpg',
  ];

  // Avatar URLs for manager and collaborators
  managerAvatarUrl: string;
  memberAvatarUrls: { [id: number]: string } = {};

  constructor(
    private equipeService: EquipesService,
    private fb: FormBuilder,
    private modalService: NgbModal,
    private dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute,
    private collaboratorService: CollaboratorService,
    private tokenStorage: TokenStorageService
  ) { }

  ngOnInit(): void {
    this.user = this.tokenStorage.getUser();
    this.teamId = +this.route.snapshot.paramMap.get('id');
    this.getTeamDetail();
    this.fetchAvailableManagers();

    this.teamForm = this.fb.group({
      nom: ['', Validators.required],
      managerEquipe: ['', Validators.required]  // Binding manager ID
    });

    this.route.queryParams.subscribe(params => {
      this.origin = params['origin'] || '';
    });
  }

  private getTeamDetail() {
    this.equipeService.getEquipeById(this.teamId).subscribe(
      data => {
        this.team = data;
        console.log('Loaded team:', this.team);
        this.teamForm.patchValue({
          nom: this.team.nom,
          managerEquipe: this.team.managerEquipe?.id || ''  // Patch the manager ID
        });

        // Compute avatars for the manager and collaborators
        this.computeAvatarUrls();
      },
      error => {
        console.error('Error fetching Equipe details', error);
      }
    );
  }

  private fetchAvailableManagers() {
    this.collaboratorService.fetchManagerEquipe().subscribe(data => {
      this.availableManagers = data.data.page.content;
      console.log(this.availableManagers);
    }, error => {
      console.error('Error fetching managers', error);
    });
  }

  updateTeamInfo() {
    if (this.teamForm.valid) {
      const updatedTeam = {
        id_equipe: this.team?.id_equipe,  // Use id_equipe instead of id
        nom: this.teamForm.value.nom,
        managerEquipe: {
          id: this.teamForm.value.managerEquipe  // Using the selected manager ID from the form
        }
      };

      console.log('Updating team with ID:', updatedTeam.id_equipe);
      console.log('Selected Manager : ', updatedTeam.managerEquipe);

      if (updatedTeam.id_equipe) {
        this.equipeService.updateTeam(updatedTeam.id_equipe, updatedTeam).subscribe(
          () => {
            this.errorMessage = '';
            this.successMessage = "Les informations de l'équipe ont été mises à jour avec succès";
            setTimeout(() => this.successMessage = '', 3000);
            this.reloadTeamDetails();
          },
          err => {
            console.error('Error updating team:', err);
            this.errorMessage = 'Les informations de profil ont échoué';
            this.successMessage = '';
            setTimeout(() => this.errorMessage = '', 3000);
          }
        );
      } else {
        console.error('Team ID is undefined, cannot update team.');
      }
    } else {
      console.log('Form is invalid:', this.teamForm.errors);
    }
  }

  reloadTeamDetails() {
    this.equipeService.getEquipeById(this.team.id_equipe).subscribe(
      response => {
        this.team = response;
        this.teamForm.patchValue({
          nom: this.team.nom,
          managerEquipe: this.team.managerEquipe?.id || ''
        });
      },
      err => {
        console.error('Error reloading team details:', err);
      }
    );
  }

  openAddCollaborators() {
    const modalRef = this.modalService.open(AddCollaboratorsModalComponent);
    modalRef.componentInstance.teamId = this.teamId;
    modalRef.componentInstance.teamName = this.team.nom;
    modalRef.result.then((result) => {
      if (result === 'success') {
        this.getTeamDetail();
      }
    }, (reason) => {
      console.log('Dismissed');
    });
  }

  openCollabDetail(member: any): void {
    const dialogRef = this.dialog.open(ProfilCollabComponent, {
      width: auto,  // Adjust width as needed
      data: {
        collaborator: member
      },
    })

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'updated') {
        this.reloadTeamDetails();  // Reload team details if collaborator info was updated
      }
    });
  }

  redirectToDepartmentManagement() {
    this.router.navigate(['/GererDepartement']);
  }

  modifyTeamModal(): void {
    const dialogRef = this.dialog.open(ModifierEquipeModalComponent, {
      width: '600px',
      data: {
        team: this.team,
        availableManagers: [] // Fetch and pass available managers here if needed
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'updated') {
        // Refresh the team details after update
        this.loadTeamDetails();
      }
    });
  }

  private loadTeamDetails() {
    window.location.reload()
  }

  openCollabDetailPage(id: number) {
    this.router.navigate(['/profilCollabManager', id]);
  }

  private computeAvatarUrls() {
    // Assign avatar to the manager
    if (this.team?.managerEquipe) {
      const index = this.team.managerEquipe.id % this.professionalAvatars.length;
      this.managerAvatarUrl = this.professionalAvatars[index];
    } else {
      // Default avatar if no manager is assigned
      this.managerAvatarUrl = '/assets/Avatars/default.png'; // Ensure the default image exists
    }

    // Assign avatars to the collaborators
    if (this.team?.collaborateurs) {
      for (let member of this.team.collaborateurs) {
        const index = member.id % this.professionalAvatars.length;
        this.memberAvatarUrls[member.id] = this.professionalAvatars[index];
      }
    }
  }

  randomGender(): string {
    return Math.random() > 0.5 ? 'male' : 'female';
  }
}
