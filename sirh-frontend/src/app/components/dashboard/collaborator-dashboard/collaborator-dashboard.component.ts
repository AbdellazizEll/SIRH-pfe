import { Component, OnInit, OnDestroy } from '@angular/core';
import { CompetenceService } from "../../../_services/competence.service";
import { FormationsServiceService } from "../../../_services/formations-service.service";
import { AbsenceService } from "../../../_services/absence.service";
import Chart from 'chart.js';

@Component({
  selector: 'app-collaborator-dashboard',
  templateUrl: './collaborator-dashboard.component.html',
  styleUrls: ['./collaborator-dashboard.component.scss']
})
export class CollaboratorDashboardComponent implements OnInit, OnDestroy {

  collaboratorDashboardData: {
    leaveBalance?: number;
    competencyGaps?: any;
    trainingOverview?: any;
  } = {};

  private charts: Chart[] = [];

  constructor(
    private competenceService: CompetenceService,
    private formationService: FormationsServiceService,
    private absenceService: AbsenceService
  ) {}

  ngOnInit(): void {
    this.loadLeaveBalance();
    this.loadCompetencyGaps();
    this.loadTrainingOverview();
  }

  ngOnDestroy(): void {
    this.charts.forEach(chart => chart.destroy());
  }

  createChart(chartId: string, type: string, data: any, options: any) {
    const existingChartIndex = this.charts.findIndex(chart => chart.canvas.id === chartId);
    if (existingChartIndex >= 0) {
      this.charts[existingChartIndex].destroy();
      this.charts.splice(existingChartIndex, 1);
    }

    const newChart = new Chart(chartId, {
      type: type,
      data: data,
      options: options
    });

    this.charts.push(newChart);
  }

  loadLeaveBalance() {
    this.absenceService.getLeaveBalance().subscribe(leaveBalance => {
      this.collaboratorDashboardData.leaveBalance = leaveBalance.leaveBalance;
      this.createChart('leaveBalanceChart', 'doughnut', {
        labels: ['Congé Utilisé', 'Congé Restant'],
        datasets: [{
          data: [leaveBalance.leaveBalance, 30 - leaveBalance.leaveBalance],
          backgroundColor: ['#FF6384', '#36A2EB']
        }]
      }, {
        responsive: true,
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          title: {
            display: true,
            text: 'Solde des Congés'
          }
        }
      });
    });
  }

  loadCompetencyGaps() {
    this.competenceService.getCompetencyGaps().subscribe(competencyGaps => {
      console.log('Competency Gaps Response:', competencyGaps);

      if (competencyGaps?.matchingCompetencies?.length > 0) {
        // Filter competencies that have a gap between collaborator and position evaluations
        const gaps = competencyGaps.matchingCompetencies.filter((competency: any) => {
          const collaboratorEval = this.convertEvaluationToNumeric(competency.collaboratorEvaluation, competency.scaleType);
          const positionEval = this.convertEvaluationToNumeric(competency.positionEvaluation, competency.scaleType);
          return collaboratorEval < positionEval;
        });

        if (gaps.length > 0) {
          const labels = gaps.map((gap: any) => gap.competenceName);
          const values = gaps.map((gap: any) => {
            const collaboratorEval = this.convertEvaluationToNumeric(gap.collaboratorEvaluation, gap.scaleType);
            const positionEval = this.convertEvaluationToNumeric(gap.positionEvaluation, gap.scaleType);
            return positionEval - collaboratorEval;
          });

          // Define distinct colors for each bar with emphasis on smaller values
          const backgroundColors = values.map((value: number) => {
            if (value <= 1) {
              return 'rgba(255, 99, 132, 1)'; // Highlight small gaps in red
            }
            return `rgba(${Math.floor(Math.random() * 150)}, ${Math.floor(Math.random() * 150)}, ${Math.floor(Math.random() * 150)}, 0.8)`; // Random color for other values
          });

          this.createChart('competencyGapChart', 'bar', {
            labels: labels,
            datasets: [{
              label: 'Écart de Compétence',
              data: values,
              backgroundColor: backgroundColors,
              borderColor: backgroundColors.map((color: any) => color.replace('0.8', '1')),
              borderWidth: 2,
              barThickness: 60, // Increase bar thickness for better visibility
            }]
          }, {
            responsive: true,
            scales: {
              y: {
                beginAtZero: false,
                min: 0.5, // Start Y-axis just below 1 to ensure small values are more noticeable
                grace: '20%', // Adds extra space to make sure the bars stand out more clearly
                title: {
                  display: true,
                  text: 'Niveau d\'Écart'
                },
                ticks: {
                  callback: function (value: any) {
                    return value.toFixed(0); // Display tick values as integers (1, 2, 3, 4)
                  },
                  stepSize: 1, // Ensure each level is represented with an integer value
                  padding: 10 // Adds more space between the y-axis labels and grid lines for better readability
                }
              },
              x: {
                ticks: {
                  autoSkip: false, // Show all labels to avoid skipping
                  maxRotation: 45, // Rotate labels for better readability
                  minRotation: 0
                }
              }
            },
            plugins: {
              legend: { display: true },
              title: { display: true, text: 'Écarts de Compétence' },
              tooltip: {
                callbacks: {
                  label: function (context: any) {
                    const index = context.dataIndex;
                    const gap = gaps[index];
                    return `${gap.competenceName}: Niveau actuel - ${gap.collaboratorEvaluation}, Niveau requis - ${gap.positionEvaluation}`;
                  }
                }
              }
            }
          });
        } else {
          console.warn('No competency gaps found after filtering.');
        }
      } else {
        console.error('No matching competencies found.');
      }
    }, error => {
      console.error('Error loading competency gaps:', error);
    });
  }

// Helper method to convert evaluation scales to numeric values
  convertEvaluationToNumeric(evaluation: string, scaleType: string): number {
    switch (scaleType) {
      case 'STARS':
        return {
          '1 STAR': 1,
          '2 STARS': 2,
          '3 STARS': 3,
          '4 STARS': 4
        }[evaluation] || 0;
      case 'NUMERIC':
        return parseInt(evaluation) || 0;
      case 'DESCRIPTIF':
        return {
          'FAIBLE': 1,
          'MOYEN': 2,
          'BON': 3,
          'EXCELLENT': 4
        }[evaluation] || 0;
      default:
        console.warn(`Unknown scale type: ${scaleType}`);
        return 0;
    }
  }


  loadTrainingOverview() {
    this.formationService.getTrainingOverview().subscribe(trainingOverview => {
      this.collaboratorDashboardData.trainingOverview = trainingOverview;
      this.createChart('trainingOverviewChart', 'pie', {
        labels: ['Formations Terminées', 'Formations en Cours'],
        datasets: [{
          data: [trainingOverview.completedTrainings, trainingOverview.inProgressTrainings],
          backgroundColor: ['#36A2EB', '#FFCE56']
        }]
      }, {
        responsive: true,
        plugins: {
          legend: { display: true },
          title: { display: true, text: 'Vue d\'Ensemble des Formations' }
        }
      });
    });
  }
}
