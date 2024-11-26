import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RegisterComponent} from "./components/register/register.component";
import {LoginComponent} from "./components/login/login.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {BoardAdminComponent} from "./components/board-admin/board-admin.component";
import {BoardManagerComponent} from "./components/board-manager/board-manager.component";
import {BoardGestionnairerhComponent} from "./components/board-gestionnairerh/board-gestionnairerh.component";
import {BoardCollaboratorComponent} from "./components/board-collaborator/board-collaborator.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {HomeComponent} from "./components/home/home.component";
import {CollaborateursComponent} from "./components/collaborateurs/collaborateurs.component";
import {ChangePasswordComponent} from "./components/collaborateurs/change-password/change-password.component";
import {AuthGuard} from "./_helpers/auth.guard";
import {AjoutAbsenceComponent} from "./components/Absence/ajout-absence/ajout-absence.component";
import {AbsenceHistoryComponent} from "./components/Absence/absence-history/absence-history.component";
import {GestionAbsenceComponent} from "./components/Absence/gestion-absence/gestion-absence.component";
import {ResponsableAbsencesComponent} from "./components/Absence/responsable-absences/responsable-absences.component";
import {RhAbsenceComponent} from "./components/Absence/rh-absence/rh-absence.component";
import {AjoutEquipeComponent} from "./components/Equipe/ajout-equipe/ajout-equipe.component";
import {DepartementComponent} from "./components/departement/departement.component";
import {GererDepartementComponent} from "./components/departement/gerer-departement/gerer-departement.component";
import {ProfileTeamComponent} from "./components/Equipe/profile-team/profile-team.component";
import {ProfilDepartementComponent} from "./components/departement/profil-departement/profil-departement.component";
import {AjoutCompetenceComponent} from "./components/Competence/ajout-competence/ajout-competence.component";
import {
  GererDetailCompetenceComponent
} from "./components/Competence/gerer-detail-competence/gerer-detail-competence.component";
import {PositionListComponent} from "./components/Poste/position-list/position-list.component";
import {AjoutPosteComponent} from "./components/Poste/ajout-poste/ajout-poste.component";
import {PositionDetailModalComponent} from "./components/Poste/position-detail-modal/position-detail-modal.component";
import {CollaboratorPosteComponent} from "./components/Poste/collaborator-poste/collaborator-poste.component";
import {GererEquipesComponent} from "./components/Equipe/gerer-equipes/gerer-equipes.component";
import {GererFormationComponent} from "./components/Formations/gerer-formation/gerer-formation.component";
import {
  CatalogueDeFormationComponent
} from "./components/Formations/Catalogue/catalogue-de-formation/catalogue-de-formation.component";
import {AjoutFormationComponent} from "./components/Formations/formations/ajout-formation/ajout-formation.component";
import {DemandeFormationComponent} from "./components/Formations/demande-formation/demande-formation.component";
import {
  ValidationFormationComponent
} from "./components/Formations/validation-formation/validation-formation.component";
import {
  SuggestionFormationComponent
} from "./components/Formations/suggestion-formation/suggestion-formation.component";
import {ViewCatalogueComponent} from "./components/Formations/Catalogue/view-catalogue/view-catalogue.component";
import {
  ListeDesFormationsComponent
} from "./components/Formations/formations/liste-des-formations/liste-des-formations.component";
import {
  ListeDemandesComponent
} from "./components/Formations/demande-formation/liste-demandes/liste-demandes.component";
import {GererCatalogueComponent} from "./components/Formations/Catalogue/gerer-catalogue/gerer-catalogue.component";
import {HistoriqueComponent} from "./components/Formations/demande-formation/historique/historique.component";
import {ManagerEquipeComponent} from "./components/Equipe/manager-equipe/manager-equipe.component";
import {
  FormationDetailsComponent
} from "./components/Formations/formations/formation-details/formation-details.component";
import {
  CompetenceDashboardModuleComponent
} from "./components/Competence/competence-dashboard-module/competence-dashboard-module.component";
import {
  AbsenceDashboardModuleComponent
} from "./components/Absence/absence-dashboard-module/absence-dashboard-module.component";
import {
  FormationDashboardModuleComponent
} from "./components/Formations/formation-dashboard-module/formation-dashboard-module.component";
import {
  CollabDashboardModuleComponent
} from "./components/collaborateurs/collab-dashboard-module/collab-dashboard-module.component";
import {MainDashboardComponent} from "./components/main-dashboard/main-dashboard.component";
import {MesFormationsComponent} from "./components/Formations/formations/mes-formations/mes-formations.component";
import {TeamEnrollmentsComponent} from "./components/Formations/team-enrollments/team-enrollments.component";
import {AppComponent} from "./app.component";
import {CollaboratorEditComponent} from "./components/collaborateurs/collaborator-edit/collaborator-edit.component";
import {ProfilCollabComponent} from "./components/Equipe/profil-collab/profil-collab.component";
import {
  ProfilCollabManagerComponent
} from "./components/Equipe/manager-equipe/ProfilCollabManager/profil-collab-manager/profil-collab-manager.component";
import {ModifyPosteComponent} from "./components/Poste/position-list/modify-poste/modify-poste/modify-poste.component";
import {
  ViewCompetenceListComponent
} from "./components/Competence/gerer-detail-competence/view-competence-list/view-competence-list.component";
import {
  TeamProfileComponent
} from "./components/departement/profil-departement/team-profile/team-profile/team-profile.component";
import {CalendarComponent} from "./components/calendar/calendar.component";
import {MesCompetencesComponent} from "./components/Competence/mes-competences/mes-competences.component";
import {
  CollaboratorDashboardComponent
} from "./components/dashboard/collaborator-dashboard/collaborator-dashboard.component";
import {ManagerDashboardComponent} from "./components/dashboard/manager-dashboard/manager-dashboard.component";
import {
  ResponsibleDashboardComponent
} from "./components/dashboard/responsible-dashboard/responsible-dashboard.component";
import {
  AbsenceDashboardComponent
} from "./components/dashboard/responsible-dashboard/absence-dashboard/absence-dashboard.component";
import {
  CompetenceDashboardComponent
} from "./components/dashboard/responsible-dashboard/competence-dashboard/competence-dashboard.component";
import {
  FormationDashboardComponent
} from "./components/dashboard/responsible-dashboard/formation-dashboard/formation-dashboard.component";
import {
  ResponsibleDepartmentComponent
} from "./components/departement/responsible-department/responsible-department.component";

  const routes: Routes = [

    {path: "register" , component:RegisterComponent },
    {path: "login", component:LoginComponent},
    {path: "dashboard", component:DashboardComponent},
    {path: "admin", component:BoardAdminComponent},
    {path: "manager", component:BoardManagerComponent},
    {path: "gestrh" , component:BoardGestionnairerhComponent},
    {path: 'collaborator' , component:BoardCollaboratorComponent},
    {path: 'profile', component:ProfileComponent, canActivate: [AuthGuard] , data: { roles: ['ROLE_RH','ROLE_MANAGER','ROLE_COLLABORATOR']}},
    {path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
    {path: 'mai', component: HomeComponent, canActivate: [AuthGuard]},
    {path: 'CollabDashboard', component: CollaboratorDashboardComponent, canActivate: [AuthGuard]},
    {path: 'ManagerDashboard', component: ManagerDashboardComponent, canActivate: [AuthGuard]},
    {path: 'collaborateurs', component:CollaborateursComponent, canActivate: [AuthGuard],data: { roles: ['ROLE_RH']},
      children: [
        {path: 'register', component: RegisterComponent}, // Default to Competence Dashboard
        {path: 'collaborateurs/edit/:id', component:CollaboratorEditComponent}
      ]
    },
    {path: 'collaborateurCompetenceDetail/:id', component:ProfilCollabComponent, canActivate: [AuthGuard],data: { roles: ['ROLE_RH','ROLE_MANAGER']}},
    {path: 'profilCollabManager/:id', component:ProfilCollabManagerComponent, canActivate: [AuthGuard]},
    {path: 'MesCompetences/:id', component:MesCompetencesComponent, canActivate: [AuthGuard]},

    {path: 'collaborateurs/edit/:id', component:CollaboratorEditComponent, canActivate: [AuthGuard],data: { roles: ['ROLE_RH']}},
    {path: 'changePassword' , component:ChangePasswordComponent, canActivate: [AuthGuard],data: { roles: ['ROLE_RH','ROLE_MANAGER','ROLE_COLLABORATOR']}},
    {path: 'GestionAbsence', component:GestionAbsenceComponent, canActivate: [AuthGuard],
      children: [
        {path: 'AjoutAbsence', component: AjoutAbsenceComponent}, // Default to Competence Dashboard
      ]
    },
    {path: 'calendar', component:CalendarComponent , canActivate: [AuthGuard] },

    {path: 'MonDepartement/:id', component:ResponsibleDepartmentComponent , canActivate: [AuthGuard] , data: { roles: ['ROLE_MANAGER'] }},

    {path: 'ResponsableAbsence', component:ResponsableAbsencesComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_MANAGER']}},
    {path: 'RhAbsence', component:RhAbsenceComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
     {path: 'AjoutAbsence' , component:AjoutAbsenceComponent , canActivate: [AuthGuard] , data: { roles: ['ROLE_RH','ROLE_COLLABORATOR','ROLE_MANAGER']}},
    {path: 'HistoryAbsence' , component:AbsenceHistoryComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH','ROLE_COLLABORATOR','ROLE_MANAGER']}},
    {path: 'AjoutEquipe', component:AjoutEquipeComponent, canActivate:[AuthGuard] , data: { roles: ['ROLE_RH']}},
      {path: 'GererEquipes', component:GererEquipesComponent, canActivate:[AuthGuard] , data: { roles: ['ROLE_RH'] }},
    {
      path: 'GererEquipes', component: GererEquipesComponent, canActivate: [AuthGuard], data: {roles: ['ROLE_RH']},
      children: [
        {path: 'AjoutEquipe', component: AjoutEquipeComponent}, // Default to Competence Dashboard
        {path: 'teamProfile/:id', component: ProfileTeamComponent},
      ]
    },
    // DEPARTMENT SIDE
    {
      path: 'GererDepartement', component: GererDepartementComponent, canActivate: [AuthGuard], data: {roles: ['ROLE_RH']},
      children: [
        {path: 'AjoutDepartment', component: DepartementComponent}, // Default to Competence Dashboard
        {path: 'ProfilDepartment/:id', component: ProfilDepartementComponent},
        {path: 'teamProfile/:id', component: ProfileTeamComponent},
        {path: 'teamProfileCard/:id', component: TeamProfileComponent},

      ]
    },
    {path: 'teamProfileCard/:id', component: TeamProfileComponent},



    {path: 'AjoutDepartment', component:DepartementComponent, canActivate:[AuthGuard], data: { roles: ['ROLE_RH']}},
    {path: 'GererDepartement', component:GererDepartementComponent, canActivate:[AuthGuard], data: { roles: ['ROLE_RH']}},
    { path: 'teamProfile/:id', component: ProfileTeamComponent, canActivate: [AuthGuard] , data: { roles: ['ROLE_RH']} },
    { path: 'ProfilDepartment/:id', component: ProfilDepartementComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_RH']} },

    ////
    {path: 'AjoutCompetence', component:AjoutCompetenceComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
    {path: 'ViewCompetenceList', component:ViewCompetenceListComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},

    {path: 'GererDetailCompetence', component:GererDetailCompetenceComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
    {
      path: 'GererDetailCompetence', component: GererDetailCompetenceComponent, canActivate: [AuthGuard], data: {roles: ['ROLE_RH']},
      children: [
        {path: 'AjoutCompetence', component: AjoutCompetenceComponent}, // Default to Competence Dashboard
      ]
    },
    {path: 'PositionList', component:PositionListComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
    {path: 'AjoutPoste', component:AjoutPosteComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
    {
      path: 'PositionList', component: PositionListComponent, canActivate: [AuthGuard], data: {roles: ['ROLE_RH']},
      children: [
        {path: 'AjoutPoste', component: AjoutPosteComponent},
        {path: 'PositionDetailModal', component:PositionDetailModalComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
// Default to Competence Dashboard
      ]
    },
    {path: 'PositionDetailModal', component:PositionDetailModalComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
    {path: 'CollaboratorPosteComp', component:CollaboratorPosteComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
    {
      path: 'PositionList', component: PositionListComponent, canActivate: [AuthGuard], data: {roles: ['ROLE_RH']},
      children: [
        {path: 'AjoutPoste', component: AjoutPosteComponent},
        {path: 'PositionDetailModal', component:PositionDetailModalComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
// Default to Competence Dashboard
      ]
    },
    {
      path: 'GererCatalogue',
      component: GererCatalogueComponent,
      canActivate: [AuthGuard],
      data: { roles: ['ROLE_RH'] },
      // No need to define child routes here unless necessary
    },

    {path: 'GererFormation', component:GererFormationComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH']}},
    {path: 'CatalogueFormation', component:CatalogueDeFormationComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH','ROLE_MANAGER']}},
    {
      path: 'ViewCatalogue/:id',
      component: ViewCatalogueComponent,
      canActivate: [AuthGuard],
      data: { roles: ['ROLE_RH', 'ROLE_MANAGER'] },
    },
    {path: 'ListDesFormations', component:ListeDesFormationsComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH','ROLE_MANAGER']}},
    {path: 'SuggestedTrainings', component:SuggestionFormationComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH','ROLE_MANAGER']}},
    {path: 'AjoutFormation', component:AjoutFormationComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_RH','ROLE_MANAGER']}},
    {path: 'AjoutDemandeFormation', component:DemandeFormationComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_MANAGER']}},
    {path: 'ListDemande', component:ListeDemandesComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_MANAGER']}},
    {path: 'GererCatalogue', component:GererCatalogueComponent , canActivate: [AuthGuard],data: { roles: ['ROLE_RH']}},
    {path: 'HistoriqueDemandesFormations', component:HistoriqueComponent , canActivate: [AuthGuard], data: { roles: ['ROLE_MANAGER']}},
    {path: 'MonEquipe/:id', component: ManagerEquipeComponent, canActivate: [AuthGuard] , data: { roles: ['ROLE_MANAGER']} },
    {path: 'FormationDetail/:id', component: FormationDetailsComponent, canActivate: [AuthGuard] , data: { roles: ['ROLE_RH','ROLE_MANAGER']}},
    {path: 'ModifierPoste', component: ModifyPosteComponent, canActivate: [AuthGuard] , data: { roles: ['ROLE_RH']}},

    {path: 'ValidationFormation', component:ValidationFormationComponent , canActivate: [AuthGuard] , data: { roles: ['ROLE_RH','ROLE_MANAGER']}},
    {path: 'mesFormations', component:MesFormationsComponent, canActivate:[AuthGuard], data: { roles: ['ROLE_COLLABORATOR']}},
    {path: 'teamEnrollments', component:TeamEnrollmentsComponent, canActivate:[AuthGuard], data: { roles: ['ROLE_MANAGER']}},

    //KPIs
    {
      path: 'main-dashboard',
      component: MainDashboardComponent,
      canActivate: [AuthGuard],
      data: { roles: ['ROLE_RH'] },
      children: [
        { path: '', redirectTo: 'competenceDashboard', pathMatch: 'full' }, // Corrected
        { path: 'competenceDashboard', component: CompetenceDashboardModuleComponent }, // Corrected
        { path: 'absenceDashboard', component: AbsenceDashboardModuleComponent },
        { path: 'trainingDashboard', component: FormationDashboardModuleComponent },
        { path: 'collaboratorDashboard', component: CollabDashboardModuleComponent },
      ],
    },

    {
      path: 'responsible-dashboard',
      component: ResponsibleDashboardComponent,
      canActivate: [AuthGuard],
      data: { roles: ['ROLE_MANAGER'] },
      children: [
        { path: '', redirectTo: 'competenceDashboard', pathMatch: 'full' }, // Corrected
        { path: 'CompetenceResponsibleDB', component: CompetenceDashboardComponent }, // Corrected
        { path: 'AbsenceResponsibleDB', component: AbsenceDashboardComponent },
        { path: 'FormationResponsibleDB', component: FormationDashboardComponent },
      ],
    },
    {path: '' , redirectTo: 'home' , pathMatch:'full'}




  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
