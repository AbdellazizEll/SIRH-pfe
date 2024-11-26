import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, startWith } from "rxjs";
import { EquipesService } from "../../../_services/equipes.service";
import { MatDialog } from "@angular/material/dialog";
import { ApiResponse } from "../../../_helpers/api-response";
import { Page } from "../../../_helpers/page";
import { HttpErrorResponse } from "@angular/common/http";
import { ModifierEquipeModalComponent } from "../modifier-equipe-modal/modifier-equipe-modal.component";
import { Router } from "@angular/router";

@Component({
  selector: 'app-gerer-equipes',
  templateUrl: './gerer-equipes.component.html',
  styleUrls: ['./gerer-equipes.component.scss']
})
export class GererEquipesComponent implements OnInit {

  teamsState$: Observable<{ appState: string, appData?: ApiResponse<Page>, error?: HttpErrorResponse }>;
  responseSubject = new BehaviorSubject<ApiResponse<Page>>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();
  notificationMessage: string = ''; // To hold error or success messages

  constructor(
    private teamService: EquipesService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadTeams();
  }

  loadTeams(): void {
    this.teamsState$ = this.teamService.getEquipes().pipe(
      map((response: any) => {
        // Log the response to check structure
        console.log('API Response:', response);
        this.responseSubject.next(response);
        this.currentPageSubject.next(response.data.page.number);
        console.log(response.data);
        return { appState: 'APP_LOADED', appData: response };
      }),
      startWith({ appState: 'APP_LOADING' }),
      catchError((error: HttpErrorResponse) => {
        return of({ appState: 'APP_ERROR', error });
      })
    );
  }

  searchTeams(name: string): void {
    this.loadTeams();
  }

  goToPage(pageNumber: number): void {
    this.loadTeams();
  }

  goToNextOrPreviousPage(direction: string): void {
    this.goToPage(direction === 'forward' ? this.currentPageSubject.value + 1 : this.currentPageSubject.value - 1);
  }

  openTeamDetailPopup(id: number): void {
    const dialogRef = this.dialog.open(ModifierEquipeModalComponent, {
      width: 'auto',
      data: { id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'updated') {
        this.loadTeams(); // Reload teams if any update has been made
      }
    });
  }

  editTeam(id: number, event: Event): void {
    event.stopPropagation();
    this.openTeamDetailPopup(id);
  }

  deleteTeam(id: number, event: Event): void {
    event.stopPropagation(); // Prevent row click from being triggered

    this.teamService.removeTeam(id).subscribe(
      () => {
        this.notificationMessage = 'Équipe supprimée avec succès';
        this.loadTeams();  // Reload the team list after deletion
      },
      (error) => {
        console.error('Error deleting team:', error);
        this.notificationMessage = 'Erreur lors de la suppression de l\'équipe';
      }
    );
  }

  addEquipe() {
    this.router.navigate(["/AjoutEquipe"]);
  }

  navigateToTeamProfile(id: number) {
    this.router.navigate(['/teamProfile', id]);
  }
}
