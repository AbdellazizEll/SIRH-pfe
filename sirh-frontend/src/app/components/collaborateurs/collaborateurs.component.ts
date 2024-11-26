import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, startWith } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { Page } from '../../_helpers/page';
import { ApiResponse } from '../../_helpers/api-response';
import { AuthService } from '../../_services/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { TokenStorageService } from '../../_services/token-storage.service';
import { Collaborator } from '../../domain/collaborator';
import { PopupComponent } from '../popup/popup.component';
import { CollabDetailComponent } from './collab-detail/collab-detail.component';
import { PositionDetailModalComponent } from '../Poste/position-detail-modal/position-detail-modal.component';
import {Router} from "@angular/router";
import {RegisterComponent} from "../register/register.component";

@Component({
  selector: 'app-collaborateurs',
  templateUrl: './collaborateurs.component.html',
  styleUrls: ['./collaborateurs.component.scss']
})
export class CollaborateursComponent implements OnInit {

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
  showModal: boolean = false;

  constructor(
    private authService: AuthService,
    private storageService: TokenStorageService,
    private dialog: MatDialog,
    private router: Router  // Inject the Router

  ) { }

  ngOnInit(): void {
    this.usersState$ = this.authService.getCollaborators().pipe(
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

  closeEditModal() {
    this.showModal = false;
  }

  detailCollab(code: any) {
    this.Openpopup(code, 'Customer Detail', CollabDetailComponent);
  }

  Openpopup(code: number, title: any, component: any) {
    const _popup = this.dialog.open(component, {
      width: '40%',
      data: {
        title: title,
        code: code
      }
    });
    _popup.afterClosed().subscribe(item => {
      // Handle after popup closed
    });
  }
  editCollab(collaboratorId: number): void {
    this.router.navigate(['/collaborateurs/edit', collaboratorId]); // Redirect to the edit page with the collaborator id
  }
  editCollab22(code: number) {
    this.Openpopup(code, 'Edit Collab', PopupComponent);
  }

  deleteCollab(id:number) {
    this.Openpopup(id,'Collaborator-Competence Details', CollabDetailComponent);
  }

  openPositionDetailModal(posteId: number): void {
    const dialogRef = this.dialog.open(PositionDetailModalComponent, {
      width: '500px',
      data: { code: posteId }
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The dialog was closed');
    });
  }
  redirectToAddCollab(){
    this.router.navigate(["register"])
}
}
