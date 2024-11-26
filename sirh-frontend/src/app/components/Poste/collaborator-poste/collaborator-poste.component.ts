import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CollaboratorService } from '../../../_services/collaborator.service';
import { CollaboratorCardComponent } from './collaborator-card/collaborator-card.component';
import { AuthService } from "../../../_services/auth.service";

@Component({
  selector: 'app-collaborator-poste',
  templateUrl: './collaborator-poste.component.html',
  styleUrls: ['./collaborator-poste.component.scss']
})
export class CollaboratorPosteComponent implements OnInit {
  collaborators: any[];

  constructor(
    private collaboratorService: CollaboratorService,
    private authService: AuthService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.getAllCollaborators();
  }

  getAllCollaborators(): void {
    this.authService.getCollaborators().subscribe(
      response => this.collaborators = response.data.page.content,
      error => console.error('Error fetching collaborators', error)
    );
  }

  openCollaboratorInfo(collaboratorId: number): void {
    const dialogRef = this.dialog.open(CollaboratorCardComponent, {
      width: '800px',
      data: {
        title: 'Collaborator-Position Détail',
        collaboratorId
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
