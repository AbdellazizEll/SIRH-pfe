import { Component, OnInit } from '@angular/core';
import { PosteService } from "../../../_services/poste.service";
import { MatDialog } from "@angular/material/dialog";
import { AjoutPositionModalComponent } from "../ajout-position-modal/ajout-position-modal.component";
import { PositionDetailModalComponent } from "../position-detail-modal/position-detail-modal.component";
import {AssignCollabComponent} from "../collaborator-poste/assign-collab/assign-collab.component";
import {Router} from "@angular/router";
import {ModifyPosteComponent} from "./modify-poste/modify-poste/modify-poste.component";
import {ConfirmDialogComponent} from "../../Absence/confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-position-list',
  templateUrl: './position-list.component.html',
  styleUrls: ['./position-list.component.scss']
})
export class PositionListComponent implements OnInit {

  positions: any[] = [];
   successMessage:String =  "";
   errorMessage:String = "";

  constructor(private posteService: PosteService, private dialog: MatDialog,
              private router:Router ) { }

  ngOnInit(): void {
    this.loadPositions();
  }

  loadPositions(): void {
    this.posteService.getAllPostes().subscribe(
      response => {
        this.positions = response;
        console.log(this.positions)
      },
      error => {
        console.error('Error fetching positions:', error);
      }
    );
  }

  deletePosition(id: number , event:Event): void {
    event.stopPropagation()

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: "Êtes-vous sûr de vouloir retirer ce collaborateur ?" }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.posteService.deletePoste(id).subscribe(
          () => {
            this.successMessage = 'Poste  retiré avec succès';
            setTimeout(() => this.successMessage = '', 3000);

            this.loadPositions();
          },
          error => {
            this.errorMessage = 'Erreur lors de la suppression du poste ';
            setTimeout(() => this.errorMessage = '', 3000);

            console.error('Error deleting position:', error);
          }
        );
      }
    })
  }

  Openpopup(code: number, title: string, component: any): void {
    const _popup = this.dialog.open(component, {
      width: '40%',
      data: {
        title: title,
        code: code
      }
    });
    _popup.afterClosed().subscribe(
      item => {
        this.loadPositions();
      },
      error => {
        console.error('Error closing popup:', error);
      }
    );
  }

  OpenpopupPosition(title: string, component: any): void {
    const _popup = this.dialog.open(component, {
      width: '40%',
      data: {
        title: title,
      }
    });
    _popup.afterClosed().subscribe(
      item => {
        this.loadPositions();
      },
      error => {
        console.error('Error closing popup:', error);
      }
    );
  }

  openPosition(): void {
    this.OpenpopupPosition('Adding Position', AjoutPositionModalComponent);
  }

  OpenPositionDetail(idPoste: number): void {
    this.Openpopup(idPoste, "View Position detail and requirements", PositionDetailModalComponent);
  }

  openAssignCollaborator(posteId: number , event: Event): void {
    event.stopPropagation(); // Prevent the row click event from being triggered
    const dialogRef = this.dialog.open(AssignCollabComponent, {
      width: '40%',
      data: { posteId: posteId, title: 'Assigner un collaborateur à ce poste' }
    });

    dialogRef.afterClosed().subscribe(
      () => {
        this.loadPositions();
      },
      error => {
        console.error('Error closing assign collaborator modal:', error);
      }
    );
  }

  redirectAddPoste() {
    this.router.navigate(["AjoutPoste"])
  }

  modifierPosition(idPoste: number, event: Event): void {
    event.stopPropagation(); // Prevent row click event

    const poste = this.positions.find(pos => pos.idPoste === idPoste); // Find the position

    const dialogRef = this.dialog.open(ModifyPosteComponent, {
      width: '40%',
      data: {
        poste,  // Pass the entire poste object to the modal
        title: 'Modifier Poste'
      }
    });

    dialogRef.afterClosed().subscribe(
      () => {
        this.loadPositions();  // Reload the list after closing the modal
      },
      error => {
        console.error('Error closing the modifier modal:', error);
      }
    );
  }
}
