import { Component, OnInit } from '@angular/core';
import { CompetenceService } from '../../../_services/competence.service';
import Chart from 'chart.js';
import { CollaboratorService } from '../../../_services/collaborator.service';
import { AuthService } from '../../../_services/auth.service';

@Component({
  selector: 'app-competence-dashboard-module',
  templateUrl: './competence-dashboard-module.component.html',
  styleUrls: ['./competence-dashboard-module.component.scss']
})
export class CompetenceDashboardModuleComponent implements OnInit {
  collaborators: any = []; // list of collaborators
  selectedCollaboratorId: number = 106; // default collaborator

  private charts: { [key: string]: Chart } = {};

  constructor(
    private competenceService: CompetenceService,
    private collaboratorService: CollaboratorService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadCollaborators();
    this.loadCompetenceCoverage(this.selectedCollaboratorId);
    this.loadCompetenceGap(this.selectedCollaboratorId);
  }

  loadCollaborators() {
    // Fetch the collaborators list from the API
    this.authService.getCollaborators().subscribe(
      response => {
        if (response && response.data && response.data.page && response.data.page.content) {
          this.collaborators = response.data.page.content.map((collab: { id: number; email: string }) => ({
            id: collab.id,
            email: collab.email
          }));
        } else {
          console.error('Unexpected response structure for collaborators:', response);
        }
      },
      error => {
        console.error('Error loading collaborators:', error);
      }
    );
  }

  onCollaboratorChange(event: any) {
    const collaboratorId = event.target.value;
    this.selectedCollaboratorId = collaboratorId;

    // Load new charts for the selected collaborator
    this.loadCompetenceCoverage(collaboratorId);
    this.loadCompetenceGap(collaboratorId);
  }

  loadCompetenceCoverage(collaboratorId: number) {
    this.competenceService.getCompetenceCoverage(collaboratorId).subscribe(data => {
      this.createCompetenceCoverageChart(data);
    });
  }

  loadCompetenceGap(collaboratorId: number) {
    this.competenceService.getCompetenceGap(collaboratorId).subscribe(data => {
      this.createCompetenceGapChart(data);
    });
  }

  createCompetenceCoverageChart(data: any) {
    // Remove the existing canvas element if it exists
    this.removeExistingChart('competenceCoverageChart');

    const labels = data.map((item: any) => item.competenceName);
    const percentages = data.map((item: any) => item.coveragePercentage);

    // Create new chart
    this.charts['competenceCoverageChart'] = new Chart('competenceCoverageChart', {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Pourcentage de couverture',
          data: percentages,
          backgroundColor: 'rgba(255, 99, 132, 0.2)',
          borderColor: 'rgba(255, 99, 132, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          yAxes: [{
            ticks: { beginAtZero: true, max: 100 },
            scaleLabel: { display: true, labelString: 'Pourcentage (%)' }
          }]
        },
        title: { display: true, text: 'Couverture des compétences' }
      }
    });
  }

  createCompetenceGapChart(data: any) {
    // Remove the existing canvas element if it exists
    this.removeExistingChart('competenceGapChart');

    const labels = data.map((item: any) => item.competenceName);
    const gaps = data.map((item: any) => item.gap);

    // Create new chart
    this.charts['competenceGapChart'] = new Chart('competenceGapChart', {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Lacune de compétence',
          data: gaps,
          backgroundColor: 'rgba(153, 102, 255, 0.2)',
          borderColor: 'rgba(153, 102, 255, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          yAxes: [{
            ticks: { beginAtZero: true, max: Math.max(...gaps) + 1 },
            scaleLabel: { display: true, labelString: 'Lacune (Niveau)' }
          }]
        },
        title: { display: true, text: 'Lacune de compétence' }
      }
    });
  }

  removeExistingChart(chartId: string) {
    if (this.charts[chartId]) {
      this.charts[chartId].destroy(); // Destroy existing chart if it exists
      delete this.charts[chartId]; // Remove reference from the charts dictionary

      // Remove the canvas element and recreate it
      const chartContainer = document.getElementById(chartId)?.parentElement;
      if (chartContainer) {
        chartContainer.innerHTML = `<canvas id="${chartId}"></canvas>`; // Replace the old canvas with a new one
      }
    }
  }
}
