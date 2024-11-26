import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, startWith } from "rxjs";
import { ApiResponse } from "../../../_helpers/api-response";
import { Page } from "../../../_helpers/page";
import { HttpErrorResponse } from "@angular/common/http";
import { Collaborator } from "../../../domain/collaborator";
import { AuthService } from "../../../_services/auth.service";
import { TokenStorageService } from "../../../_services/token-storage.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-gerer-detail-competence',
  templateUrl: './gerer-detail-competence.component.html',
  styleUrls: ['./gerer-detail-competence.component.scss']
})
export class GererDetailCompetenceComponent implements OnInit {
  usersState$: Observable<{ appState: string, appData?: ApiResponse<Page>, error?: HttpErrorResponse }>;
  selectedCollaborator: Collaborator = {
    id: 0,
    firstname: null,
    lastname: null,
    age: null,
    email: null,
    password: null,
    isCollab: false,
    isEnabled: false,
    isGestRH: false,
    isManager: false,
    phone: null,
    createdAt: new Date()
  };

  responseSubject = new BehaviorSubject<ApiResponse<Page>>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();

  constructor(
    private authService: AuthService,
    private storageService: TokenStorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.usersState$ = this.authService.getCollaborators().pipe(
      map((response: any) => {
        this.responseSubject.next(response);
        this.currentPageSubject.next(response.data.page.number);
        return { appState: 'APP_LOADED', appData: response };
      }),
      startWith({ appState: 'APP_LOADING' }),
      catchError((error: HttpErrorResponse) => {
        console.error('Error loading collaborators:', error);
        return of({ appState: 'APP_ERROR', error });
      })
    );
  }

  getStatus(isEnabled: boolean): string {
    return isEnabled ? 'ACTIVE' : 'INACTIVE';
  }

  getStatusClass(isEnabled: boolean): string {
    return isEnabled ? 'bg-success' : 'bg-danger';
  }

  goToPage(name?: string, pageNumber: number = 0): void {
    this.usersState$ = this.authService.getCollaborators(name, pageNumber).pipe(
      map((response: ApiResponse<Page>) => {
        this.responseSubject.next(response);
        this.currentPageSubject.next(pageNumber);
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

  openCollabDetailPage(id: number): void {
    this.router.navigate(['/collaborateurCompetenceDetail', id]);
  }

  redirectToAddCompetence() {
    this.router.navigate(['AjoutCompetence']);
  }

  // New method to navigate to existing competencies page
  redirectToExistingCompetencies() {
    this.router.navigate(['ViewCompetenceList']); // Adjust the route path to match your app's routing configuration
  }
}
