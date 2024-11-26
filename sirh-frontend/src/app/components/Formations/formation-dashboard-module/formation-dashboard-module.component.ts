import { Component, OnInit } from '@angular/core';
import { FormationsServiceService } from '../../../_services/formations-service.service';
import Chart from 'chart.js';

@Component({
  selector: 'app-formation-dashboard-module',
  templateUrl: './formation-dashboard-module.component.html',
  styleUrls: ['./formation-dashboard-module.component.scss']
})
export class FormationDashboardModuleComponent implements OnInit {

  constructor(private formationService: FormationsServiceService) {}

  ngOnInit(): void {
    this.loadEnrollmentCountByDepartment();
    this.loadImprovementRateByDepartment();
  }

  // Load and create chart for Enrollment Count by Department
  loadEnrollmentCountByDepartment() {
    this.formationService.getEnrollmentCountByDepartment().subscribe(data => {
      const labels = data.map((item: any) => item.departmentName);
      const counts = data.map((item: any) => item.enrollmentCount);

      new Chart('enrollmentByDepartmentChart', {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Nombre d\'inscriptions',
            data: counts,
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
                beginAtZero: true,
                stepSize: 1
              },
              scaleLabel: {
                display: true,
                labelString: 'Nombre d\'inscriptions'
              }
            }]
          },
          plugins: {
            legend: { display: true },
            title: { display: true, text: 'Nombre d\'inscriptions par département' }
          }
        }
      });
    });
  }

  // Load and create chart for Competence Improvement Rate by Department
  loadImprovementRateByDepartment() {
    this.formationService.getOverallCompetenceImprovementRateByDepartment().subscribe(data => {
      const labels = data.map((item: any) => item.departmentName);
      const rates = data.map((item: any) => item.improvementRate);

      new Chart('improvementRateByDepartmentChart', {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Taux d\'amélioration des compétences (%)',
            data: rates,
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
                beginAtZero: true,
                max: 100,
                stepSize: 10
              },
              scaleLabel: {
                display: true,
                labelString: 'Pourcentage (%)'
              }
            }]
          },
          plugins: {
            legend: { display: true },
            title: { display: true, text: 'Taux d\'amélioration des compétences par département' }
          }
        }
      });
    });
  }
}
