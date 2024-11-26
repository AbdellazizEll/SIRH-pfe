import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, startWith } from 'rxjs/operators';

import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import {ApiResponse} from "../../../../_helpers/api-response";
import {CompetenceService} from "../../../../_services/competence.service";
import {Page} from "../../../../_helpers/page";
import {ModifierCompetenceComponent} from "../../modifier-competence/modifier-competence.component";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../../../Absence/confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-view-competence-list',
  templateUrl: './view-competence-list.component.html',
  styleUrls: ['./view-competence-list.component.scss']
})
export class ViewCompetenceListComponent implements OnInit {
  competenceState$: Observable<{ appState: string, appData?: ApiResponse<Page>, error?: HttpErrorResponse }>;
  responseSubject = new BehaviorSubject<ApiResponse<Page>>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();
   errorMessage: string;
   successMessage: string;

  constructor(private competenceService: CompetenceService,
              private router: Router,
              private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadCompetences();
  }

  loadCompetences(name?: string, pageNumber: number = 0): void {
    this.competenceState$ = this.competenceService.getAllCompetences().pipe(
      map((response: any) => {
        this.responseSubject.next(response);
        this.currentPageSubject.next(response.data.page.number);
        console.log(response.data);
        return { appState: 'APP_LOADED', appData: response };
      }),
      startWith({ appState: 'APP_LOADING' }),
      catchError((error: HttpErrorResponse) => {
        console.error('Error loading collaborators:', error);
        return of({ appState: 'APP_ERROR', error });
      })
    );
  }

  redirectToAddCompetence() {
    this.router.navigate(['AjoutCompetence']);
  }

  searchCompetences(name: string): void {
    this.loadCompetences(name);
  }

  editCompetence(competence: any): void {
    const dialogRef = this.dialog.open(ModifierCompetenceComponent, {
      width: '500px',
      data: competence  // Pass the entire competence object to the dialog
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'updated') {
        // Refresh the competence list if the competence was modified
        this.loadCompetences();
      }
    });
  }
  deleteCompetence(id: number): void {
    event.stopPropagation(); // Prevent the row click event from being triggered
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: "Êtes-vous sûr de vouloir retirer cette compétence  ?" }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.competenceService.deleteCompetence(id).subscribe(
          () => {
            this.errorMessage = '';
            this.successMessage = "l'équipe a été retiré avec succés";
            setTimeout(() => this.successMessage = '', 3000);
            this.loadCompetences();
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

  goToPage(name?: string, pageNumber: number = 0): void {
    this.competenceState$ = this.competenceService.getAllCompetences(name, pageNumber).pipe(
      map((response: ApiResponse<Page>) => {
        this.responseSubject.next(response);
        this.currentPageSubject.next(pageNumber);
        console.log(response);
        return { appState: 'APP_LOADED', appData: response };
      }),
      startWith({ appState: 'APP_LOADED', appData: this.responseSubject.value }),
      catchError((error: HttpErrorResponse) => {
        console.error('Error loading collaborators:', error);
        return of({ appState: 'APP_ERROR', error });
      })
    );
  }

  goToNextOrPreviousPage(direction?: string, name?: string): void {
    this.goToPage(name, direction === 'forward' ? this.currentPageSubject.value + 1 : this.currentPageSubject.value - 1);
  }

  redirectToAddCollab() {
    this.router.navigate(['/AjoutCompetence'])
  }

  mapEvaluationValue(value: string): string {
    const evaluationMap: { [key: string]: string } = {
      'stars': 'Étoiles',
      'numeric': 'Numérique',
      'descriptif': 'Description'
    };
    return evaluationMap[value.toLowerCase()] || value;
  }
}
