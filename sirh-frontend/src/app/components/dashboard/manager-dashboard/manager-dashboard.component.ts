import { Component, OnInit, AfterViewInit } from '@angular/core';
import { TokenStorageService } from "../../../_services/token-storage.service";
import { FormationsServiceService } from "../../../_services/formations-service.service";
import { CompetenceService } from "../../../_services/competence.service";
import { AbsenceService } from "../../../_services/absence.service";
import { EquipesService } from "../../../_services/equipes.service";
import Chart from 'chart.js';

@Component({
  selector: 'app-manager-dashboard',
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.scss']
})
export class ManagerDashboardComponent implements OnInit, AfterViewInit {
  userId: any;
  team: any = []; // Store the team data (including manager and collaborators)
  selectedCollaborator: any = null;

  private charts: Chart[] = [];

  constructor(
    private tokenStorage: TokenStorageService,
    private formationService: FormationsServiceService,
    private competenceService: CompetenceService,
    private absenceService: AbsenceService,
    private equipeService: EquipesService
  ) {}

  ngOnInit(): void {
    this.userId = this.tokenStorage.getUser().id;
    this.loadTeamData(this.userId);
  }

  ngAfterViewInit(): void {
    // After view init ensures all HTML elements are rendered
  }

  loadTeamData(managerId: number): void {
    this.equipeService.getEquipeByManager(managerId).subscribe(
      (response) => {
        this.team = response.collaborateurs;
        console.log('Team Data:', this.team);
      },
      (error) => {
        console.error('Error loading team data', error);
      }
    );
  }

  onCollaboratorChange(event: any) {
    const collaboratorId = event.target.value;
    this.selectedCollaborator = this.team.find((collab: any) => collab.id == collaboratorId);
    if (this.selectedCollaborator) {
      setTimeout(() => {
        this.loadKPIsForCollaborator(collaboratorId);
      }, 100); // Ensures HTML containers are available
    }
  }

  loadKPIsForCollaborator(collaboratorId: number): void {
    // Clear previous charts
    this.clearCharts();

    // Competence Coverage KPI
    this.competenceService.getCompetenceCoverage(collaboratorId).subscribe(competenceCoverage => {
      console.log('Competence Coverage:', competenceCoverage);
      setTimeout(() => {
        this.createChart('competenceCoverageChart', 'bar', {
          labels: competenceCoverage.map((cc: any) => cc.competenceName),
          datasets: [{
            label: 'Pourcentage de couverture',
            data: competenceCoverage.map((cc: any) => cc.coveragePercentage),
            backgroundColor: 'rgba(255, 99, 132, 0.6)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
          }]
        });
      }, 100);
    });

    // Competence Gap KPI
    this.competenceService.getCompetenceGap(collaboratorId).subscribe(competenceGap => {
      console.log('Competence Gap:', competenceGap);
      setTimeout(() => {
        this.createChart('competenceGapChart', 'bar', {
          labels: competenceGap.map((cg: any) => cg.competenceName),
          datasets: [{
            label: 'Lacune de compétence',
            data: competenceGap.map((cg: any) => cg.gap),
            backgroundColor: 'rgba(153, 102, 255, 0.6)',
            borderColor: 'rgba(153, 102, 255, 1)',
            borderWidth: 1
          }]
        });
      }, 100);
    });

    // Training Completion Rate KPI
    this.formationService.getTrainingCompletionRate(collaboratorId).subscribe(completionRate => {
      console.log('Training Completion Rate:', completionRate);
      setTimeout(() => {
        this.createChart('trainingCompletionChart', 'doughnut', {
          labels: ['Formations terminées', 'Formations en cours'],
          datasets: [{
            data: [completionRate, 100 - completionRate],
            backgroundColor: ['rgba(75, 192, 192, 0.8)', 'rgba(201, 203, 207, 0.8)']
          }]
        });
      }, 100);
    });

    // Average Training Progress KPI
    this.formationService.getAverageTrainingProgress(collaboratorId).subscribe(averageProgress => {
      console.log('Average Training Progress:', averageProgress);
      setTimeout(() => {
        this.createChart('averageTrainingProgressChart', 'bar', {
          labels: ['Progrès moyen des formations'],
          datasets: [{
            label: 'Progrès moyen (%)',
            data: [averageProgress],
            backgroundColor: 'rgba(255, 206, 86, 0.6)',
            borderColor: 'rgba(255, 206, 86, 1)',
            borderWidth: 1
          }]
        });
      }, 100);
    });

    // Absenteeism Rate KPI
    this.absenceService.getAbsenteismRateManager(collaboratorId).subscribe(absenteeismRate => {
      console.log('Absenteeism Rate:', absenteeismRate);
      setTimeout(() => {
        this.createChart('absenteeismRateChart', 'pie', {
          labels: ['Taux d\'absentéisme', 'Présence'],
          datasets: [{
            data: [absenteeismRate, 100 - absenteeismRate],
            backgroundColor: ['rgba(255, 99, 132, 0.6)', 'rgba(75, 192, 192, 0.6)']
          }]
        });
      }, 100);
    });

    // Average Absence Duration KPI
    this.absenceService.getAverageAbsenceManager(collaboratorId).subscribe(avgAbsenceDuration => {
      console.log('Average Absence Duration:', avgAbsenceDuration);
      setTimeout(() => {
        this.createChart('averageAbsenceChart', 'bar', {
          labels: ['Durée moyenne des absences'],
          datasets: [{
            label: 'Durée moyenne (jours)',
            data: [avgAbsenceDuration],
            backgroundColor: 'rgba(54, 162, 235, 0.6)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
          }]
        });
      }, 100);
    });
  }

  createChart(chartId: string, type: string, data: any): void {
    const chartElement = document.getElementById(chartId) as HTMLCanvasElement;
    if (!chartElement) {
      console.error(`Chart with id ${chartId} not found in the DOM.`);
      return;
    }

    const chart = new Chart(chartElement, {
      type: type,
      data: data,
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          title: {
            display: true,
            text: chartId.replace(/([A-Z])/g, ' $1')
          }
        },
        scales: type === 'bar' ? {
          yAxes: [{
            ticks: {
              beginAtZero: true,
              stepSize: 10,
              callback: function (value: number) {
                return value % 1 === 0 ? value : null; // Only display whole numbers
              }
            },
            scaleLabel: {
              display: true,
              labelString: 'Niveau'
            }
          }],
          xAxes: [{
            scaleLabel: {
              display: true,
              labelString: 'Compétences' // Custom label for the x-axis if needed
            }
          }]
        } : {}
      }
    });

    this.charts.push(chart);
  }

  clearCharts(): void {
    this.charts.forEach(chart => chart.destroy());
    this.charts = [];
  }
}
