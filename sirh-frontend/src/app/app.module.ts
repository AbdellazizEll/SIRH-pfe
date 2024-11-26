import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './components/register/register.component';
import {NgbModalModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { BoardAdminComponent } from './components/board-admin/board-admin.component';
import { BoardGestionnairerhComponent } from './components/board-gestionnairerh/board-gestionnairerh.component';
import { BoardManagerComponent } from './components/board-manager/board-manager.component';
import { BoardCollaboratorComponent } from './components/board-collaborator/board-collaborator.component';
import { HomeComponent } from './components/home/home.component';

import { authInterceptorProviders } from './_helpers/auth.interceptor';
import { ProfileComponent } from './components/profile/profile.component';
import { CollaborateursComponent } from './components/collaborateurs/collaborateurs.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { PopupComponent } from './components/popup/popup.component';
import { CollabDetailComponent } from './components/collaborateurs/collab-detail/collab-detail.component';
import {MaterialModule} from "./material-modules";
import { ChangePasswordComponent } from './components/collaborateurs/change-password/change-password.component';
import { AjoutAbsenceComponent } from './components/Absence/ajout-absence/ajout-absence.component';
import { AbsenceHistoryComponent } from './components/Absence/absence-history/absence-history.component';
import { GestionAbsenceComponent } from './components/Absence/gestion-absence/gestion-absence.component';
import {MatSelectModule} from "@angular/material/select";
import {MatInputModule} from "@angular/material/input";
import {MatDatepicker, MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {MatButtonModule} from "@angular/material/button";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatFormFieldModule} from "@angular/material/form-field";
import { ResponsableAbsencesComponent } from './components/Absence/responsable-absences/responsable-absences.component';
import { RhAbsenceComponent } from './components/Absence/rh-absence/rh-absence.component';
import { AjoutEquipeComponent } from './components/Equipe/ajout-equipe/ajout-equipe.component';
import { DepartementComponent } from './components/departement/departement.component';
import { GererDepartementComponent } from './components/departement/gerer-departement/gerer-departement.component';
import { ProfileTeamComponent } from './components/Equipe/profile-team/profile-team.component';
import { ProfilDepartementComponent } from './components/departement/profil-departement/profil-departement.component';
import { AddTeamModalComponent } from './components/departement/add-team-modal/add-team-modal.component';
import { AddCollaboratorsModalComponent } from './components/Equipe/add-collaborators-modal/add-collaborators-modal.component';
import { AjoutCompetenceComponent } from './components/Competence/ajout-competence/ajout-competence.component';
import { RadarChartComponent } from './components/radar-chart/radar-chart.component';
import { ChartsModule } from 'ng2-charts';
import { GererDetailCompetenceComponent } from './components/Competence/gerer-detail-competence/gerer-detail-competence.component';
import { PositionListComponent } from './components/Poste/position-list/position-list.component';
import { AjoutPosteComponent } from './components/Poste/ajout-poste/ajout-poste.component';
import { AjoutPositionModalComponent } from './components/Poste/ajout-position-modal/ajout-position-modal.component';
import { PositionDetailModalComponent } from './components/Poste/position-detail-modal/position-detail-modal.component';
import { CollaboratorPosteComponent } from './components/Poste/collaborator-poste/collaborator-poste.component';
import {MatTreeModule} from "@angular/material/tree";
import { CollaboratorCardComponent } from './components/Poste/collaborator-poste/collaborator-card/collaborator-card.component';
import { ComparisonChartComponent } from './components/radar-chart/comparison-chart/comparison-chart/comparison-chart.component';
import { CapitalizeFirstPipe } from './capitalize-first.pipe';
import { AssignCollabComponent } from './components/Poste/collaborator-poste/assign-collab/assign-collab.component';
import { ProfilCollabComponent } from './components/Equipe/profil-collab/profil-collab.component';
import { GererEquipesComponent } from './components/Equipe/gerer-equipes/gerer-equipes.component';
import { ModifierEquipeModalComponent } from './components/Equipe/modifier-equipe-modal/modifier-equipe-modal.component';
import { AjoutFormationComponent } from './components/Formations/formations/ajout-formation/ajout-formation.component';
import { GererFormationComponent } from './components/Formations/gerer-formation/gerer-formation.component';
import { CatalogueDeFormationComponent } from './components/Formations/Catalogue/catalogue-de-formation/catalogue-de-formation.component';
import { DemandeFormationComponent } from './components/Formations/demande-formation/demande-formation.component';
import { ValidationFormationComponent } from './components/Formations/validation-formation/validation-formation.component';
import { SuggestionFormationComponent } from './components/Formations/suggestion-formation/suggestion-formation.component';
import { ViewCatalogueComponent } from './components/Formations/Catalogue/view-catalogue/view-catalogue.component';
import { ListeDesFormationsComponent } from './components/Formations/formations/liste-des-formations/liste-des-formations.component';
import { ModifierFormationModalComponent } from './components/Formations/formations/modifier-formation-modal/modifier-formation-modal.component';
import { ListeDemandesComponent } from './components/Formations/demande-formation/liste-demandes/liste-demandes.component';
import { GererCatalogueComponent } from './components/Formations/Catalogue/gerer-catalogue/gerer-catalogue.component';
import { CatalogueModalComponent } from './components/Formations/Catalogue/catalogue-modal/catalogue-modal.component';
import { ConfirmModalComponent } from './components/Formations/Catalogue/confirm-modal/confirm-modal.component';
import { HistoriqueComponent } from './components/Formations/demande-formation/historique/historique.component';
import { ManagerEquipeComponent } from './components/Equipe/manager-equipe/manager-equipe.component';
import { FormationDetailsComponent } from './components/Formations/formations/formation-details/formation-details.component';
import { FormationDashboardModuleComponent } from './components/Formations/formation-dashboard-module/formation-dashboard-module.component';
import { CompetenceDashboardModuleComponent } from './components/Competence/competence-dashboard-module/competence-dashboard-module.component';
import { AbsenceDashboardModuleComponent } from './components/Absence/absence-dashboard-module/absence-dashboard-module.component';
import {
  CollabDashboardModuleComponent
} from "./components/collaborateurs/collab-dashboard-module/collab-dashboard-module.component";
import { MainDashboardComponent } from './components/main-dashboard/main-dashboard.component';
import {MesFormationsComponent} from "./components/Formations/formations/mes-formations/mes-formations.component";
import {
  CompetenceHistoryModalComponent
} from "./components/Formations/formations/mes-formations/competence-history-modal/competence-history-modal.component";
import { TeamEnrollmentsComponent } from './components/Formations/team-enrollments/team-enrollments.component';
import { UpdateProgressModalComponent } from './components/Formations/team-enrollments/update-progress-modal/update-progress-modal.component';
import { CollaboratorEditComponent } from './components/collaborateurs/collaborator-edit/collaborator-edit.component';
import { DemandeModifierComponent } from './components/Formations/demande-formation/demande-modifier/demande-modifier.component';
import { ModifierDepartementComponent } from './components/departement/modifier-departement/modifier-departement.component';
import { ModifyTeamModalComponent } from './components/Equipe/modify-team-modal/modify-team-modal.component';
import { RequestRejectionComponent } from './components/Formations/demande-formation/request-rejection/request-rejection.component';
import { ProfilCollabManagerComponent } from './components/Equipe/manager-equipe/ProfilCollabManager/profil-collab-manager/profil-collab-manager.component';
import { ModifyPosteComponent } from './components/Poste/position-list/modify-poste/modify-poste/modify-poste.component';
import {NgOptimizedImage} from "@angular/common";
import { ViewCompetenceListComponent } from './components/Competence/gerer-detail-competence/view-competence-list/view-competence-list.component';
import {
  TeamProfileComponent
} from "./components/departement/profil-departement/team-profile/team-profile/team-profile.component";
import { ConfirmDialogComponent } from './components/Absence/confirm-dialog/confirm-dialog.component';
import { RejectionDialogComponent } from './components/Absence/rejection-dialog/rejection-dialog.component';
import { ModifierCompetenceComponent } from './components/Competence/modifier-competence/modifier-competence.component';
import {MatIconModule} from "@angular/material/icon";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import { RejectConfirmationModalComponent } from './components/Formations/team-enrollments/reject-confirmation-modal/reject-confirmation-modal.component';
import {
  ScheduleModule,
  DayService,
  WeekService,
  WorkWeekService,
  MonthService,
  AgendaService,
  TimelineMonthService, TimelineViewsService, MonthAgendaService
} from '@syncfusion/ej2-angular-schedule';
import { CalendarComponent } from './components/calendar/calendar.component';
import { MesCompetencesComponent } from './components/Competence/mes-competences/mes-competences.component';
import { CollaboratorDashboardComponent } from './components/dashboard/collaborator-dashboard/collaborator-dashboard.component';
import { ManagerDashboardComponent } from './components/dashboard/manager-dashboard/manager-dashboard.component';
import { ResponsibleDashboardComponent } from './components/dashboard/responsible-dashboard/responsible-dashboard.component';
import { AbsenceDashboardComponent } from './components/dashboard/responsible-dashboard/absence-dashboard/absence-dashboard.component';
import { FormationDashboardComponent } from './components/dashboard/responsible-dashboard/formation-dashboard/formation-dashboard.component';
import { CompetenceDashboardComponent } from './components/dashboard/responsible-dashboard/competence-dashboard/competence-dashboard.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import { ResponsibleDepartmentComponent } from './components/departement/responsible-department/responsible-department.component';


@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    DashboardComponent,
    BoardAdminComponent,
    BoardGestionnairerhComponent,
    BoardManagerComponent,
    BoardCollaboratorComponent,
    HomeComponent,
    ProfileComponent,
    CollaborateursComponent,
    PopupComponent,
    CollabDetailComponent,
    ChangePasswordComponent,
    AjoutAbsenceComponent,
    AbsenceHistoryComponent,
    GestionAbsenceComponent,
    ResponsableAbsencesComponent,
    RhAbsenceComponent,
    AjoutEquipeComponent,
    DepartementComponent,
    GererDepartementComponent,
    ProfileTeamComponent,
    ProfilDepartementComponent,
    AddTeamModalComponent,
    AddCollaboratorsModalComponent,
    AjoutCompetenceComponent,
    RadarChartComponent,
    GererDetailCompetenceComponent,
    PositionListComponent,
    AjoutPosteComponent,
    AjoutPositionModalComponent,
    PositionDetailModalComponent,
    CollaboratorPosteComponent,
    CollaboratorCardComponent,
    ComparisonChartComponent,
    CapitalizeFirstPipe,
    AssignCollabComponent,
    ProfilCollabComponent,
    GererEquipesComponent,
    ModifierEquipeModalComponent,
    AjoutFormationComponent,
    GererFormationComponent,
    CatalogueDeFormationComponent,
    DemandeFormationComponent,
    ValidationFormationComponent,
    SuggestionFormationComponent,
    ViewCatalogueComponent,
    ListeDesFormationsComponent,
    ModifierFormationModalComponent,
    ListeDemandesComponent,
    GererCatalogueComponent,
    CatalogueModalComponent,
    ConfirmModalComponent,
    HistoriqueComponent,
    ManagerEquipeComponent,
    FormationDetailsComponent,
    FormationDashboardModuleComponent,
    CompetenceDashboardModuleComponent,
    AbsenceDashboardModuleComponent,
    CollabDashboardModuleComponent,
    MainDashboardComponent,
    MesFormationsComponent,
    CompetenceHistoryModalComponent,
    TeamEnrollmentsComponent,
    UpdateProgressModalComponent,
    CollaboratorEditComponent,
    DemandeModifierComponent,
    ModifierDepartementComponent,
    ModifyTeamModalComponent,
    RequestRejectionComponent,
    ProfilCollabManagerComponent,
    ModifyPosteComponent,
    ViewCompetenceListComponent,
    TeamProfileComponent,
    ConfirmDialogComponent,
    RejectionDialogComponent,
    ModifierCompetenceComponent,
    RejectConfirmationModalComponent,
    CalendarComponent,
    MesCompetencesComponent,
    CollaboratorDashboardComponent,
    ManagerDashboardComponent,
    ResponsibleDashboardComponent,
    AbsenceDashboardComponent,
    FormationDashboardComponent,
    CompetenceDashboardComponent,
    ResponsibleDepartmentComponent

  ],
    imports: [
      NgOptimizedImage,
      BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        NgbModule,
        ReactiveFormsModule,
        FormsModule,
        NgbModalModule,
        BrowserAnimationsModule,
        MatDialogModule,
        MaterialModule,
        MatSelectModule,
        MatInputModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatButtonModule,
        MatCheckboxModule,
        MatFormFieldModule,
        NgbModule,
        ChartsModule,
        MatTreeModule,
      MatIconModule,
      MatProgressBarModule,
      ScheduleModule,
      MatProgressSpinnerModule

    ],
  providers: [authInterceptorProviders ,
    DayService, WeekService, WorkWeekService, MonthService, AgendaService, MonthAgendaService, TimelineViewsService, TimelineMonthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
