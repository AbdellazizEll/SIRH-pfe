import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { EquipesService } from "../../../_services/equipes.service";
import { ProfilCollabComponent } from "../profil-collab/profil-collab.component";
import { auto } from "@popperjs/core";
import { MatDialog } from "@angular/material/dialog";

@Component({
  selector: 'app-manager-equipe',
  templateUrl: './manager-equipe.component.html',
  styleUrls: ['./manager-equipe.component.scss']
})
export class ManagerEquipeComponent implements OnInit {
  managerId: number;
  team: any;  // Store the team data (including manager and collaborators)
  manager: any;

  // Properties to store avatar URLs
  managerAvatarUrl: string;
  memberAvatarUrls: { [id: number]: string } = {};


  professionalAvatars: string[] = [
    '/assets/Avatars/asset1.jpg',
    '/assets/Avatars/asset2.jpg',
    '/assets/Avatars/asset3.jpg',
    '/assets/Avatars/asset4.jpg',
    '/assets/Avatars/asset5.jpg',
    '/assets/Avatars/asset6.jpg',
  ];
  constructor(
    private route: ActivatedRoute,
    private equipeService: EquipesService,
    private router: Router,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    // Get the manager's ID from the route parameter
    this.managerId = +this.route.snapshot.paramMap.get('id');

    // Load the team data for the given manager
    this.loadTeamData(this.managerId);
  }

  loadTeamData(managerId: number): void {
    this.equipeService.getEquipeByManager(managerId).subscribe(
      (response) => {
        this.team = response;
        this.manager = this.team.managerEquipe; // Assuming the manager data is here

        // Compute avatar URLs
        this.computeAvatarUrls();
      },
      (error) => {
        console.error('Error loading team data', error);
      }
    );
  }

  private computeAvatarUrls() {
    // Assign a professional avatar to the manager
    if (this.manager) {
      const index = this.manager.id % this.professionalAvatars.length;
      this.managerAvatarUrl = this.professionalAvatars[index];
    } else {
      // Default avatar if no manager
      this.managerAvatarUrl = '../assets/Avatars/default.png'; // Make sure default.jpg exists
    }

    // Assign professional avatars to collaborators
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

  viewProfile(member: any) {
    const dialogRef = this.dialog.open(ProfilCollabComponent, {
      width: auto,
      data: {
        collaborator: member
      },
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'updated') {
        // Handle any updates if necessary
      }
    });
  }

  openCollabDetailPage(id: any) {
    this.router.navigate(['/profilCollabManager', id]);
  }
}
