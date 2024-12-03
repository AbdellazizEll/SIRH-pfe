import { Component, AfterViewInit } from '@angular/core';
import Chart from 'chart.js';
import { CollaboratorService } from "../../../_services/collaborator.service";

@Component({
  selector: 'app-collaborator-dashboard',
  templateUrl: './collab-dashboard-module.component.html',
  styleUrls: ['./collab-dashboard-module.component.scss']
})
export class CollabDashboardModuleComponent implements AfterViewInit {

  constructor(private collaboratorService: CollaboratorService) {
  }

  ngAfterViewInit(): void {
    this.loadMostAbsences();
    this.loadLeastAbsences();
    this.loadHighestTrainingCompletion();
    this.loadHighestCompetenceGrowth();
  }

  loadMostAbsences() {
    this.collaboratorService.MostAbsenceCollab().subscribe(response => {
      if (response.length === 0) {
        document.getElementById('mostAbsencesChartMessage')!.innerText = "Aucun collaborateur n'a d'absences enregistrées.";
        return;
      }

      const labels = response.map((collab: any) => `${collab.firstname} ${collab.lastname}`);
      const absences = response.map((collab: any) => collab.absenceCount);

      new Chart('mostAbsencesChart', {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Nombre d\'absences',
            data: absences,
            backgroundColor: '#FF6384',
            borderColor: '#FF6384',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: true
              },
              scaleLabel: {
                display: true,
                labelString: 'Absences'
              }
            }]
          },
          plugins: {
            legend: {
              display: false
            },
            title: {
              display: true,
              text: 'Collaborateurs avec le plus d\'absences'
            }
          }
        }
      });
    });
  }

  loadLeastAbsences() {
    this.collaboratorService.LeastAbsenceCollab().subscribe(response => {
      if (response.length === 0) {
        document.getElementById('leastAbsencesChartMessage')!.innerText = "Aucun collaborateur n'a d'absences enregistrées.";
        return;
      }

      const labels = response.map((collab: any) => `${collab.firstname} ${collab.lastname}`);
      const absences = response.map((collab: any) => collab.absenceCount);

      new Chart('leastAbsencesChart', {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Nombre d\'absences',
            data: absences,
            backgroundColor: '#36A2EB',
            borderColor: '#36A2EB',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: true
              },
              scaleLabel: {
                display: true,
                labelString: 'Absences'
              }
            }]
          },
          plugins: {
            legend: {
              display: false
            },
            title: {
              display: true,
              text: 'Collaborateurs avec le moins d\'absences'
            }
          }
        }
      });
    });
  }

  loadHighestTrainingCompletion() {
    this.collaboratorService.TrainingCompleted().subscribe(response => {
      if (!response || response.totalCompletedTrainings === 0) {
        document.getElementById('highestTrainingCompletionMessage')!.innerText = "Aucun collaborateur n'a encore terminé une formation. Encourageons-les à commencer une formation aujourd'hui !";
        return;
      }

      new Chart('highestTrainingCompletionChart', {
        type: 'doughnut',
        data: {
          labels: [`${response.firstname} ${response.lastname}`],
          datasets: [{
            label: 'Formations Terminées',
            data: [response.totalCompletedTrainings],
            backgroundColor: ['#FFCE56']
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              display: true,
              position: 'top',
            },
            title: {
              display: true,
              text: 'Collaborateur avec le plus de formations terminées'
            }
          }
        }
      });
    }, error => {
      console.error('Error loading highest training completion:', error);
      document.getElementById('highestTrainingCompletionMessage')!.innerText = "Erreur lors du chargement des données de formation. Veuillez réessayer plus tard.";
    });
  }

  loadHighestCompetenceGrowth() {
    this.collaboratorService.highestCompetenceGrowth().subscribe(
      response => {
        if (!response) {
          // Handle no content case, where API returned 204 No Content
          document.getElementById('highestCompetenceGrowthMessage')!.innerText = "Aucun collaborateur n'a encore enregistré de croissance des compétences. Encourageons-les à se développer davantage !";
          return;
        }

        // Assuming the response is a single object with growth details
        const collaborator = response;
        const growth = response.growth;

        new Chart('highestCompetenceGrowthChart', {
          type: 'line',
          data: {
            labels: [`${collaborator.firstname} ${collaborator.lastname}`],
            datasets: [{
              label: 'Croissance des compétences',
              data: [growth],
              backgroundColor: '#4BC0C0',
              borderColor: '#4BC0C0',
              fill: false,
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
              yAxes: [{
                ticks: {
                  beginAtZero: true
                },
                scaleLabel: {
                  display: true,
                  labelString: 'Croissance des compétences'
                }
              }]
            },
            plugins: {
              legend: {
                display: false
              },
              title: {
                display: true,
                text: 'Collaborateur avec la plus grande croissance des compétences'
              }
            }
          }
        });
      },
      error => {
        console.error('Error loading highest competence growth:', error);
        document.getElementById('highestCompetenceGrowthMessage')!.innerText = "Erreur lors du chargement des données de croissance des compétences. Veuillez réessayer plus tard.";
      }
    );
  }

}
