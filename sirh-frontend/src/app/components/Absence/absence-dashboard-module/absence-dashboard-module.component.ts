import { Component, OnInit } from '@angular/core';
import { AbsenceService } from "../../../_services/absence.service";
import Chart from 'chart.js'; // Importing auto for compatibility

@Component({
  selector: 'app-absence-dashboard-module',
  templateUrl: './absence-dashboard-module.component.html',
  styleUrls: ['./absence-dashboard-module.component.scss']
})
export class AbsenceDashboardModuleComponent implements OnInit {

  constructor(private absenceService: AbsenceService) { }

  ngOnInit(): void {
    this.loadAbsenceRate();
    this.loadAverageAbsenceDuration();
    this.loadAbsenceByDepartment();
    this.loadTopAbsenceReasons();
    this.loadAbsenceRateByDepartment();
  }

  // Absence Rate Chart
  loadAbsenceRate() {
    this.absenceService.getAbsenceRate().subscribe(rate => {
      new Chart('absenceRateChart', {
        type: 'doughnut',
        data: {
          labels: ['Taux d\'absence', 'Taux de présence'],
          datasets: [{
            label: 'Taux d\'absence',
            data: [rate, 100 - rate],
            backgroundColor: ['#FF6384', '#36A2EB'],
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
              text: 'Taux d\'absence'
            }
          }
        }
      });
    });
  }

  // Average Absence Duration Chart
  loadAverageAbsenceDuration() {
    this.absenceService.getAverageAbsenceDuration().subscribe((data: any[]) => {
      const labels = data.map(item => item.departmentName);
      const values = data.map(item => item.averageDuration);

      new Chart('averageAbsenceDurationChart', {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Durée moyenne (jours)',
            data: values,
            backgroundColor: '#FFCE56'
          }]
        },
        options: {
          responsive: true,
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: true
              },
              scaleLabel: {
                display: true,
                labelString: 'Jours'
              }
            }]
          },
          plugins: {
            legend: { display: true },
            title: { display: true, text: 'Durée d\'absence moyenne par département' }
          }
        }
      });
    });
  }

  // Absence by Department Chart
  loadAbsenceByDepartment() {
    this.absenceService.getAbsenceByDepartment().subscribe(data => {
      const labels = data.map((dept: any) => dept.departmentName);
      const values = data.map((dept: any) => dept.totalAbsenceDays);

      new Chart('absenceByDepartmentChart', {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Total des jours d\'absence',
            data: values,
            backgroundColor: '#36A2EB'
          }]
        },
        options: {
          responsive: true,
          scales: {
            yAxes: [{
              ticks: { beginAtZero: true }
            }]
          },
          plugins: {
            legend: { display: true },
            title: { display: true, text: 'Absentéisme par département' }
          }
        }
      });
    });
  }

  // Top Reasons for Absence Chart
  loadTopAbsenceReasons() {
    this.absenceService.topAbsenceReasons().subscribe(data => {
      const labels = data.map((reason: any) => reason.typeAbsence);
      const values = data.map((reason: any) => reason.absenceCount);

      new Chart('topAbsenceReasonsChart', {
        type: 'pie',
        data: {
          labels: labels,
          datasets: [{
            label: 'Principales raisons d\'absence',
            data: values,
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'],
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: { display: true },
            title: { display: true, text: 'Principales raisons d\'absence' }
          }
        }
      });
    });
  }

  // Absence Rate by Department Chart
  loadAbsenceRateByDepartment() {
    this.absenceService.getAbsenceRateByDepartment().subscribe(data => {
      const labels = data.map((dept: any) => dept.departmentName);
      const values = data.map((dept: any) => dept.absenceRate);

      new Chart('absenceRateByDepartmentChart', {
        type: 'horizontalBar',  // Using horizontal bar for easy comparison between departments
        data: {
          labels: labels,
          datasets: [{
            label: 'Taux d\'absence (%)',
            data: values,
            backgroundColor: '#8E44AD'
          }]
        },
        options: {
          responsive: true,
          scales: {
            xAxes: [{
              ticks: {
                beginAtZero: true,
                callback: function(value) {
                  return value + "%"; // Adds '%' to the value
                }
              },
              scaleLabel: {
                display: true,
                labelString: 'Pourcentage'
              }
            }]
          },
          plugins: {
            legend: { display: true },
            title: { display: true, text: 'Taux d\'absence par département' }
          }
        }
      });
    });
  }
}
