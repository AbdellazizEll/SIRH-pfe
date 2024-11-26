// liste-des-formations.component.ts

import { Component, OnInit } from '@angular/core';
import { FormationsServiceService } from '../../../../_services/formations-service.service';
import { BehaviorSubject, catchError, map, Observable, of, startWith } from 'rxjs';
import { ApiResponse } from '../../../../_helpers/api-response';
import { Page } from '../../../../_helpers/page';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { CatalogueServiceService } from "../../../../_services/catalogue-service.service";
import { CompetenceService } from '../../../../_services/competence.service';
import { ModifierFormationModalComponent } from "../modifier-formation-modal/modifier-formation-modal.component";
import { Router } from "@angular/router";

@Component({
  selector: 'app-liste-des-formations',
  templateUrl: './liste-des-formations.component.html',
  styleUrls: ['./liste-des-formations.component.scss']
})
export class ListeDesFormationsComponent implements OnInit {

  formationsState$: Observable<{ appState: string, appData?: ApiResponse<Page>, error?: HttpErrorResponse }>;
  responseSubject = new BehaviorSubject<ApiResponse<Page>>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();

  query: string = '';
  selectedCompetenceId: number | null = null;
  selectedCatalogue: number | null = null;
  competences: any[] = [];
  catalogues: any[] = [];
  errorMessage: string = '';

  successMessage: string ='';

  constructor(
    private catalogueService: CatalogueServiceService,
    private formationService: FormationsServiceService,
    private competenceService: CompetenceService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCatalogues();
    this.loadCompetences();
    this.loadFormations();
  }

  mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      'English': 'Anglais',
      'French': 'Français',
      'JAVA': 'Java',
      'Soft Skills': 'Compétences Douces'
    };
    return competenceMap[name] || name;
  }

  goToPage(pageNumber: number = 0): void {
    this.formationsState$ = this.formationService
      .getAllFormations(this.query, this.selectedCompetenceId, this.selectedCatalogue, pageNumber)
      .pipe(
        map((response: ApiResponse<Page>) => {
          // Ensure content is an array
          if (!Array.isArray(response.data.page.content)) {
            response.data.page.content = [];
          }
        console.log(response)
          this.responseSubject.next(response);
          this.currentPageSubject.next(pageNumber);
          return { appState: 'APP_LOADED', appData: response };
        }),
        startWith({ appState: 'APP_LOADING' }),
        catchError((error: HttpErrorResponse) => {
          console.error('Error loading formations:', error);
          return of({ appState: 'APP_ERROR', error });
        })
      );
  }

  loadCompetences(): void {
    this.competenceService.getAllCompetences().subscribe(
      (response) => {
        console.log('Competences response:', response);
        this.competences = Array.isArray(response.data?.page?.content) ? response.data.page.content : [];
      },
      (error) => {
        console.error('Error loading competences:', error);
      }
    );
  }
  private loadCatalogues(): void {
    this.catalogueService.getAllCatalogues().subscribe(
      (response) => {
        this.catalogues = Array.isArray(response.data?.catalogues) ? response.data.catalogues : [];
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement des catalogues.';
        console.error(error);
      }
    );
  }

  private loadFormations(): void {
    this.goToPage(0);
  }

  onFilterChange(): void {
    this.goToPage(0);
  }

  goToNextOrPreviousPage(direction?: string): void {
    const pageChange = direction === 'forward'
      ? this.currentPageSubject.value + 1
      : this.currentPageSubject.value - 1;
    this.goToPage(pageChange);
  }

  clearFilter(): void {
    this.selectedCompetenceId = null;
    this.selectedCatalogue = null;
    this.query = '';
    this.goToPage(0);
  }

  viewFormationDetails(id: number): void {
    const dialogRef = this.dialog.open(ModifierFormationModalComponent, {
      width: 'auto',
      data: { id }
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      if (result === 'updated') {
        this.loadFormations();
      }
    });
  }

  deleteFormation(id: number, event: Event): void {
    event.stopPropagation();
    if (confirm('Are you sure you want to delete this formation?')) {
      this.formationService.deleteFormation(id).subscribe(() => {
        this.loadFormations();
      });
    }
  }



  redirectToAjoutFormation(): void {
    this.router.navigate(["/AjoutFormation"]);
  }

  protected readonly Array = Array;
}
