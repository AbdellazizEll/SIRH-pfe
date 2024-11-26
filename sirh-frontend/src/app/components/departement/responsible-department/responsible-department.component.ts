import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from "../../../_services/token-storage.service";
import { CollaboratorService } from "../../../_services/collaborator.service";
import { DepartmentService } from "../../../_services/department.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-responsible-department',
  templateUrl: './responsible-department.component.html',
  styleUrls: ['./responsible-department.component.scss']
})
export class ResponsibleDepartmentComponent implements OnInit {

  department: any; // Store the fetched department details
  userId: number;  // Store the logged-in user ID

  // Avatar URLs for responsible and teams
  responsableAvatarUrl = './assets/Avatars/default.png'; // Placeholder URL for avatar images
  teamAvatarUrls: { [key: number]: string } = {}; // Store avatar URLs for each team

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

  constructor(
    private tokenStorage: TokenStorageService,
    private collaboratorService: CollaboratorService,
    private departmentService: DepartmentService,
    private router:Router
  ) { }

  ngOnInit(): void {
    // Get the logged-in user ID
    const user = this.tokenStorage.getUser();
    if (user && user.id) {
      this.userId = user.id;
      this.loadUserDetails();
    }
  }

  /**
   * Load user details including department information
   */
  loadUserDetails(): void {
    this.collaboratorService.findById(this.userId).subscribe(
      (response) => {
        if (response && response.responsibleDepartment) {
          this.loadDepartmentDetails(response.responsibleDepartment.id_dep);
        }
      },
      (error) => {
        console.error('Error fetching user details:', error);
      }
    );
  }

  /**
   * Load department details by department ID
   * @param departmentId ID of the department
   */
  loadDepartmentDetails(departmentId: number): void {
    this.departmentService.getDepartmentById(departmentId).subscribe(
      (department) => {
        this.department = department;
        // Compute avatars for department responsible and teams
        this.computeAvatarUrls();
      },
      (error) => {
        console.error('Error fetching department details:', error);
      }
    );
  }

  /**
   * Compute avatar URLs for department responsible and teams
   */
  computeAvatarUrls(): void {
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
      this.department.equipes.forEach((team: any) => {
        const index = team.id_equipe % this.teamAvatars.length;
        this.teamAvatarUrls[team.id_equipe] = this.teamAvatars[index];
      });
    }
  }

  /**
   * Open the collaborator detail page
   * @param collaboratorId ID of the collaborator to open
   */
  openCollabDetailPage(collaboratorId: number): void {
    // Navigate to the collaborator detail page, e.g., using Angular Router
    this.router.navigate(['/profilCollabManager', collaboratorId]);
  }

  /**
   * Open the team profile page
   * @param teamId ID of the team to open
   */
  openTeamProfile(teamId: number): void {
    // Navigate to the team profile page, e.g., using Angular Router
    this.router.navigate(['teamProfileCard', teamId]);
  }


}
