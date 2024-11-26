import { Component, OnInit } from '@angular/core';
import { DepartmentService } from "../../../_services/department.service";
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import {ModifierDepartementComponent} from "../modifier-departement/modifier-departement.component";
import {ConfirmDialogComponent} from "../../Absence/confirm-dialog/confirm-dialog.component";
import {EquipesService} from "../../../_services/equipes.service";

@Component({
  selector: 'app-gerer-departement',
  templateUrl: './gerer-departement.component.html',
  styleUrls: ['./gerer-departement.component.scss']
})
export class GererDepartementComponent implements OnInit {
  departmentData: any[] = [];
  currentPage = 0;
  totalPages = 1;
   successMessage: string ="";
   errorMessage: string="";

  constructor(
    private departmentService: DepartmentService,
    private router: Router,
    private dialog: MatDialog,
    private equipeService:EquipesService
  ) { }

  ngOnInit(): void {
    this.loadDepartments();
  }

  private loadDepartments() {
    this.departmentService.getDepartments().pipe(
      catchError((err) => {
        alert("Departments Introuvable");
        return throwError(err);
      })
    ).subscribe(
      (response) => {
        this.departmentData = response.content;
        this.totalPages = response.totalPages;
        console.log(this.departmentData); // For debugging
      }
    );
  }

  goToPage(page: number) {
    this.currentPage = page;
    this.loadDepartments();
  }

  goToPreviousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadDepartments();
    }
  }

  goToNextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadDepartments();
    }
  }

  viewTeamDetails(teamId: number) {
    this.router.navigate(['teamProfile', teamId]);
  }

  viewDepartmentDetails(departmentId: number) {
    this.router.navigate(['ProfilDepartment', departmentId]);
  }

  deleteDepartment(id: number , event: Event) {
    event.stopPropagation(); // Prevent the row click event from being triggered

    // if (confirm('Êtes-vous sûr de vouloir supprimer ce département?')) {
    //   this.departmentService.(id).subscribe(
    //     (response) => {
    //       this.loadDepartments(this.currentPage); // Reload departments after deletion
    //     },
    //     (err) => {
    //       alert("Erreur lors de la suppression");
    //     }
    //   );
    // }
  }

  ajoutDepartement() {
    this.router.navigate(["AjoutDepartment"])

  }

  modifierDepartement(department: any, event: Event) {
    event.stopPropagation(); // Prevent the row click event from being triggered

    const dialogRef = this.dialog.open(ModifierDepartementComponent, {
      width: '600px',
      data: { department } // Pass the department data to the modal
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'updated') {
        this.loadDepartments(); // Reload departments after update
      }
    });
  }

  deleteTeam(id_dep:number, event:Event) {
    event.stopPropagation(); // Prevent the row click event from being triggered
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: "Êtes-vous sûr de vouloir retirer cette équipe  ?" }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.equipeService.removeTeam(id_dep).subscribe(
          () => {
            this.errorMessage = '';
            this.successMessage = "l'équipe a été retiré avec succés";
            setTimeout(() => this.successMessage = '', 3000);
            this.loadDepartments();
          },
          (error) => {
            console.error('Error deleting team:', error);
            alert('Error deleting team');
            this.errorMessage = "erreur lors de la supression l'équipe "

          }
        );
      }
    })
  }
}
