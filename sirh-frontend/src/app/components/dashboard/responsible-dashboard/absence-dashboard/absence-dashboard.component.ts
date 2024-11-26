import {Component, OnInit, ViewChild} from '@angular/core';
import {Color, Label} from "ng2-charts";
import {ChartDataSets, ChartOptions, ChartType} from "chart.js";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {AbsenceService} from "../../../../_services/absence.service";
import {TokenStorageService} from "../../../../_services/token-storage.service";

@Component({
  selector: 'app-absence-dashboard',
  templateUrl: './absence-dashboard.component.html',
  styleUrls: ['./absence-dashboard.component.scss']
})
export class AbsenceDashboardComponent implements OnInit {

  // Dashboard Data
  totalAbsenceDays: any;
  absenceTrends: any= [];

  // Chart Data and Configuration for Absence Trends
  absenceTrendChartLabels: Label[] = [];
  absenceTrendChartData: ChartDataSets[] = [];
  absenceTrendChartType: ChartType = 'line';
  absenceTrendChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      xAxes: [{ ticks: { beginAtZero: true } }],
      yAxes: [{ ticks: { beginAtZero: true } }]
    }
  };
  absenceTrendChartColors: Color[] = [
    { backgroundColor: 'rgba(54,162,235,0.4)', borderColor: 'rgba(54,162,235,1)' }
  ];

  // Loading Indicators and Error Messages
  isLoading: boolean = false;
  errorMessage: string = '';

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private absenceService: AbsenceService,
    private tokenStorage: TokenStorageService

  ) { }

  ngOnInit(): void {
    this.initializeDashboard();
  }

  /**
   * Initialize the dashboard by fetching total absence days and absence trends.
   */
  initializeDashboard(): void {
    this.isLoading = true;

    // Fetch Total Absence Days
    this.absenceService.getTotalAbsenceDays().subscribe(
      response => {
        this.totalAbsenceDays = response;
      },
      error => {
        console.error('Error fetching total absence days:', error);
        this.errorMessage = 'Failed to load total absence days.';
      }
    );

    // Fetch Absence Trends
    this.absenceService.getAbsenceTrends().subscribe(
      data => {
        this.absenceTrends = data;
        this.prepareAbsenceTrendsChart();
        this.isLoading = false;
      },
      error => {
        console.error('Error fetching absence trends:', error);
        this.errorMessage = 'Failed to load absence trends.';
        this.isLoading = false;
      }
    );
  }

  /**
   * Prepare data for the Absence Trends Chart.
   */
  prepareAbsenceTrendsChart(): void {
    this.absenceTrendChartLabels = this.absenceTrends.map((trend: any) => trend.month);
    this.absenceTrendChartData = [
      { data: this.absenceTrends.map((trend: any) => trend.absenceDays), label: 'Absence Days' }
    ];
  }

}
