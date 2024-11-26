// src/app/competence-dashboard/competence-dashboard.component.ts
import { Component, OnInit, ViewChild } from '@angular/core';
import { ChartOptions, ChartType, ChartDataSets } from 'chart.js';
import { Label, Color } from 'ng2-charts';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {CompetenceService} from "../../../../_services/competence.service";
import {CollaboratorService} from "../../../../_services/collaborator.service";
import {DepartmentService} from "../../../../_services/department.service";
import {TokenStorageService} from "../../../../_services/token-storage.service";

@Component({
  selector: 'app-competence-dashboard',
  templateUrl: './competence-dashboard.component.html',
  styleUrls: ['./competence-dashboard.component.scss']
})
export class CompetenceDashboardComponent implements OnInit {

  // User and Department Details
  collaboratorId: number; // Replace with actual logic to get logged-in user ID
  departmentId: number;
  teams: any[] = [];

  // Dashboard Data
  departmentCompetenceCoverage: any = [];
  teamCompetenceGaps: any = [];
  teamCompetenceCoverage: any= [];
  teamCompetenceImprovementRates: any= [];

  // Chart Data and Configuration
  competenceCoverageChartLabels: Label[] = [];
  competenceCoverageChartData: number[] = [];
  competenceCoverageChartType: ChartType = 'bar';
  competenceCoverageChartOptions: ChartOptions = {
    responsive: true,
    scales: { yAxes: [{ ticks: { beginAtZero: true } }] }
  };
  competenceCoverageChartColors: Color[] = [
    { backgroundColor: 'rgba(63,81,181,0.6)' }
  ];

  competenceImprovementChartLabels: Label[] = [];
  competenceImprovementChartData: number[] = [];
  competenceImprovementChartType: ChartType = 'radar';
  competenceImprovementChartOptions: ChartOptions = {
    responsive: true,
  };
  competenceImprovementChartColors: Color[] = [
    { backgroundColor: 'rgba(255,99,132,0.2)', borderColor: 'rgba(255,99,132,1)' }
  ];


  competenceGapChartLabels: Label[] = [];
  competenceGapChartData: ChartDataSets[] = [];
  competenceGapChartType: ChartType = 'bar';
  competenceGapChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      xAxes: [{ stacked: true }],
      yAxes: [{ stacked: true, ticks: { beginAtZero: true } }]
    }
  };
  competenceGapChartColors: Color[] = [
    { backgroundColor: 'rgba(244,67,54,0.6)' },
    { backgroundColor: 'rgba(33,150,243,0.6)' }
  ];

  // Loading Indicators and Error Messages
  isLoading: boolean = false;
  errorMessage: string = '';

  // Table Data Sources
  departmentCompetenceCoverageDataSource: MatTableDataSource<any>;
  teamCompetenceGapsDataSource: MatTableDataSource<any>;

  // Table Columns
  coverageDisplayedColumns: string[] = ['competenceId', 'competenceName', 'coveragePercentage'];
  gapsDisplayedColumns: string[] = ['competenceId', 'competenceName', 'gap', 'numberOfCollaborators'];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private competenceService: CompetenceService,
    private collaboratorService: CollaboratorService
    ,
  private departmentService: DepartmentService,
    private tokenStorage:TokenStorageService
  ) { }

  ngOnInit(): void {
    this.collaboratorId = this.tokenStorage.getUser().id
    this.initializeDashboard();
  }

  /**
   * Initialize the dashboard by fetching user and department data.
   */
  initializeDashboard(): void {
    this.isLoading = true;
    // Replace with actual logic to get the logged-in collaborator ID

    // Fetch Collaborator Details
    this.collaboratorService.findById(this.collaboratorId).subscribe(
      response => {
        console.log(response)
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
          // Preload data for the first team by default
          const defaultTeamId = this.teams[0].id_equipe;
          this.selectedTeamId(defaultTeamId);
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

  onCollaboratorChange(event: any): void {
    const teamId = event.target.value;
    if (teamId) {
      this.selectedTeamId(teamId);
    }
  }

  selectedTeamId(teamId: number): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.loadDashboardData(teamId);
  }

  loadDashboardData(teamId: number): void {
    // Fetch Competence Coverage by Team
    this.competenceService.getCompetenceCoverageByTeam(teamId).subscribe(
      data => {
        this.teamCompetenceCoverage = data;
        this.prepareCompetenceCoverageChart();
      },
      error => {
        console.error('Error fetching team competence coverage:', error);
        this.errorMessage = 'Failed to load team competence coverage.';
      }
    );

    // Fetch Competence Improvement Rates for Team
    this.competenceService.getCompetenceImprovementRatesForTeam(teamId).subscribe(
      data => {
        this.teamCompetenceImprovementRates = data;
        this.prepareCompetenceImprovementChart();
        this.isLoading = false;
      },
      error => {
        console.error('Error fetching team competence improvement rates:', error);
        this.errorMessage = 'Failed to load team competence improvement rates.';
        this.isLoading = false;
      }
    );

    this.competenceService.getCompetenceGapsForTeam(teamId).subscribe(
      data => {
        this.teamCompetenceGaps = data;
        this.prepareCompetenceGapsChart();
        this.isLoading = false;
      },
      error => {
        console.error('Error fetching team competence gaps:', error);
        this.errorMessage = 'Failed to load team competence gaps.';
        this.isLoading = false;
      }
    );
  }

  prepareCompetenceCoverageChart(): void {
    this.competenceCoverageChartLabels = this.teamCompetenceCoverage.map((c: any) => c.competenceName);
    this.competenceCoverageChartData = this.teamCompetenceCoverage.map((c: any) => c.coveragePercentage);
  }

  prepareCompetenceImprovementChart(): void {
    this.competenceImprovementChartLabels = this.teamCompetenceImprovementRates.map((c: any) => c.competenceName);
    this.competenceImprovementChartData = this.teamCompetenceImprovementRates.map((c: any) => c.improvementRate);
  }

  prepareCompetenceGapsChart(): void {
    this.competenceGapChartLabels = this.teamCompetenceGaps.map((c: any) => c.competenceName);
    const gaps = this.teamCompetenceGaps.map((c: any) => c.gap);
    const numberOfCollaborators = this.teamCompetenceGaps.map((c: any) => c.numberOfCollaborators);

    this.competenceGapChartData = [
      { data: gaps, label: 'Taille des lacunes' },
      { data: numberOfCollaborators, label: 'Nombre de collaborateur' }
    ];
  }
}
