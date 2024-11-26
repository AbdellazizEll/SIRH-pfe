import { Component, OnInit } from '@angular/core';
import { FormationsServiceService } from "../../../../_services/formations-service.service";
import { DepartmentService } from "../../../../_services/department.service";
import { TokenStorageService } from "../../../../_services/token-storage.service";
import { ChartType } from 'chart.js';
import { CollaboratorService } from "../../../../_services/collaborator.service";

@Component({
  selector: 'app-formation-dashboard',
  templateUrl: './formation-dashboard.component.html',
  styleUrls: ['./formation-dashboard.component.scss']
})
export class FormationDashboardComponent implements OnInit {

  // Dashboard Data
  trainingCompletionRate: number = 0;
  competenceImprovementRates: any[] = [];
  enrollmentCount: number = 0;

  // User and Department Details
  departmentId: number;
  teams: any[] = [];

  // Loading Indicators and Error Messages
  isLoading: boolean = false;
  errorMessage: string = '';

  // Chart Data and Configuration for Competence Improvement
  competenceImprovementChartLabels: string[] = [];
  competenceImprovementChartData: any[] = [];
  competenceImprovementChartType: ChartType = 'bar';
  competenceImprovementChartOptions: any = {
    responsive: true,
    scales: {
      xAxes: [{ ticks: { beginAtZero: true } }],
      yAxes: [{ ticks: { beginAtZero: true } }]
    }
  };
  competenceImprovementChartColors: any[] = [
    { backgroundColor: 'rgba(75,192,192,0.4)', borderColor: 'rgba(75,192,192,1)' }
  ];

  // Chart Data and Configuration for Enrollment Count
  enrollmentChartLabels: string[] = [];
  enrollmentChartData: any[] = [];
  enrollmentChartType: ChartType = 'bar';
  enrollmentChartOptions: any = {
    responsive: true,
    scales: {
      xAxes: [{ ticks: { beginAtZero: true } }],
      yAxes: [{ ticks: { beginAtZero: true } }]
    }
  };
  enrollmentChartColors: any[] = [
    { backgroundColor: 'rgba(54,162,235,0.4)', borderColor: 'rgba(54,162,235,1)' }
  ];

  collaboratorId: any;

  constructor(
    private formationService: FormationsServiceService,
    private departmentService: DepartmentService,
    private tokenStorage: TokenStorageService,
    private collaboratorService: CollaboratorService
  ) { }

  ngOnInit(): void {
    this.collaboratorId = this.tokenStorage.getUser().id;
    this.initializeDashboard();
  }

  /**
   * Initialize the dashboard by fetching department and team data.
   */
  initializeDashboard(): void {
    this.isLoading = true;

    this.collaboratorService.findById(this.collaboratorId).subscribe(
      response => {
        this.departmentId = response.responsibleDepartment.id_dep;
        this.fetchDepartmentDetails(this.departmentId);
      },
      error => {
        console.error('Error fetching collaborator details:', error);
        this.errorMessage = 'Failed to load user details.';
        this.isLoading = false;
      }
    );
  }

  /**
   * Fetch department details, including teams.
   * @param departmentId The ID of the department.
   */
  fetchDepartmentDetails(departmentId: number): void {
    this.departmentService.getDepartmentById(departmentId).subscribe(
      department => {
        this.teams = department.equipes;
        if (this.teams.length > 0) {
          // Load data for the first team by default
          const defaultTeamId = this.teams[0].id_equipe;
          this.loadDashboardData(defaultTeamId);
        } else {
          this.errorMessage = 'No teams found for the department.';
          this.isLoading = false;
        }
      },
      error => {
        console.error('Error fetching department details:', error);
        this.errorMessage = 'Failed to load department details.';
        this.isLoading = false;
      }
    );
  }

  /**
   * Set the selected team and load dashboard data.
   * @param event Event triggered by team selection.
   */
  onTeamChange(event: any): void {
    const teamId = event.target.value;
    if (teamId) {
      this.loadDashboardData(teamId);
    }
  }

  /**
   * Load all dashboard data based on the selected team.
   * @param teamId The ID of the selected team.
   */
  loadDashboardData(teamId: number): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Fetch Training Completion Rate
    this.formationService.getTrainingCompletionRateForTeam(teamId).subscribe(
      response => {
        this.trainingCompletionRate = response;
      },
      error => {
        console.error('Error fetching training completion rate:', error);
        this.errorMessage = 'Failed to load training completion rate.';
      }
    );

    // Fetch Competence Improvement Rates
    this.formationService.getCompetenceImprovementRatesForTeam(teamId).subscribe(
      data => {
        this.competenceImprovementRates = data;
        this.prepareCompetenceImprovementChart();
        this.isLoading = false;
      },
      error => {
        console.error('Error fetching competence improvement rates:', error);
        this.errorMessage = 'Failed to load competence improvement rates.';
        this.isLoading = false;
      }
    );

    // Fetch Enrollment Count
    this.formationService.getEnrollmentCountByTeam(teamId).subscribe(
      response => {
        this.enrollmentCount = response;
        this.prepareEnrollmentChart(teamId);
      },
      error => {
        console.error('Error fetching enrollment count:', error);
        this.errorMessage = 'Failed to load enrollment count.';
        this.isLoading = false;
      }
    );
  }

  /**
   * Prepare data for the Competence Improvement Chart.
   */
  prepareCompetenceImprovementChart(): void {
    this.competenceImprovementChartLabels = this.competenceImprovementRates.map(rate => rate.competenceName);
    this.competenceImprovementChartData = [
      { data: this.competenceImprovementRates.map(rate => rate.improvementRate), label: 'Taux d\'amélioration (%)' }
    ];
  }

  /**
   * Prepare data for the Enrollment Chart.
   */
  prepareEnrollmentChart(teamId: number): void {
    this.enrollmentChartLabels = [this.teams.find(team => team.id_equipe === teamId).nom];
    this.enrollmentChartData = [
      { data: [this.enrollmentCount], label: 'Nombre d\'inscriptions' }
    ];
  }
}
