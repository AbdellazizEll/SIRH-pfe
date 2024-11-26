import { Component, Input, OnInit, OnChanges, SimpleChanges, ChangeDetectorRef } from '@angular/core';
import { ChartOptions, ChartType, ChartDataSets } from 'chart.js';
import { Label } from 'ng2-charts';

@Component({
  selector: 'app-radar-chart',
  templateUrl: './radar-chart.component.html',
  styleUrls: ['./radar-chart.component.scss']
})
export class RadarChartComponent implements OnInit, OnChanges {
  @Input() collaborator: any;
  @Input() competences: any[];

  @Input() poste:any;

  public radarChartOptions: ChartOptions = {
    responsive: true,
    scale: {
      ticks: {
        beginAtZero: true,
        max: 5,
        stepSize: 1
      },
      pointLabels: {
        fontSize: 14 // Adjust font size for better readability
      }
    }
  };
  public radarChartLabels: Label[] = [];
  public radarChartType: ChartType = 'radar';
  public radarChartData: ChartDataSets[] = [
    {
      data: [],
      label: 'Compétences Requises',
      borderColor: 'rgba(54, 162, 235, 1)',
      backgroundColor: 'rgba(54, 162, 235, 0.2)'
    },
    {
      data: [],
      label: 'Compétences Collaborateur',
      borderColor: 'rgba(255, 99, 132, 1)',
      backgroundColor: 'rgba(255, 99, 132, 0.2)'  // Ensure these colors are distinct
    }
  ];

  constructor(private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.setChartData();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['competences'] && !changes['competences'].firstChange) {
      this.setChartData();
    }
  }

  private setChartData(): void {
    if (this.competences && this.competences.length > 0) {
      const labels: string[] = [];
      const data: number[] = [];

      this.competences.forEach((competence: any) => {
        labels.push(this.mapCompetenceName(competence.competence.name));
        data.push(this.getCompetenceLevel(competence));
      });

      this.radarChartLabels = labels;
      this.radarChartData[0].data = data;

      console.log('Radar Chart Labels:', this.radarChartLabels);
      console.log('Radar Chart Data:', this.radarChartData[0].data);
    } else {
      console.log('No competences provided');
    }

    // Force change detection to update the view
    this.cdr.detectChanges();
  }

  private getCompetenceLevel(competence: any): number {
    switch (competence.scaleType) {
      case 'STARS':
        return parseInt(competence.evaluation.split(' ')[0], 10);
      case 'NUMERIC':
        return parseInt(competence.evaluation, 10);
      case 'DESCRIPTIF':
        return this.convertDescriptiveToNumeric(competence.evaluation);
      default:
        return 0;
    }
  }

  private convertDescriptiveToNumeric(evaluation: string): number {
    switch (evaluation.toUpperCase()) {
      case 'EXCELLENT':
        return 5;
      case 'BON':
        return 3;
      case 'MOYEN':
        return 2;
      case 'FAIBLE':
        return 1;
      default:
        return 0;
    }
  }

  private mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      'English': 'Anglais',
      'French': 'Français',
      'JAVA': 'Java',
      'Soft Skills': 'Compétences Douces'
      // Add more mappings as necessary
    };
    return competenceMap[name] || name;
  }
}
