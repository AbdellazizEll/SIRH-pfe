import { Component, Input, OnInit, OnChanges, SimpleChanges, ChangeDetectorRef, ViewChild } from '@angular/core';
import { ChartOptions, ChartType, ChartDataSets } from 'chart.js';
import { Label, BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-comparison-chart',
  templateUrl: './comparison-chart.component.html',
  styleUrls: ['./comparison-chart.component.scss']
})
export class ComparisonChartComponent implements OnInit, OnChanges {
  @Input() collaborator: any;
  @Input() competences: any[];
  @Input() poste: any;

  @ViewChild(BaseChartDirective) chart: BaseChartDirective;

  public radarChartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    scale: {
      ticks: {
        beginAtZero: true,
        max: 5,
        stepSize: 1,
        fontSize: 12,
        fontColor: '#555'
      },
      pointLabels: {
        fontSize: 12,
        fontColor: '#555'
      }
    },
    legend: {
      position: 'top',
      labels: {
        fontSize: 12,
        fontColor: '#555'
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
      backgroundColor: 'rgba(255, 99, 132, 0.2)'
    }
  ];

  constructor(private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.setChartData();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['competences'] || changes['poste']) {
      console.log('Competences or Poste changed:', changes);
      this.setChartData();
    }
  }

  private setChartData(): void {
    if (this.competences && this.poste && this.poste.requiredCompetence) {
      const labels: string[] = [];
      const requiredData: number[] = [];
      const collaboratorData: number[] = [];

      this.poste.requiredCompetence.forEach((competence: any) => {
        labels.push(this.mapCompetenceName(competence.competence.name));
        requiredData.push(this.getCompetenceLevel(competence));
      });

      labels.forEach((label) => {
        const collaboratorCompetence = this.competences.find(comp => this.mapCompetenceName(comp.competence.name) === label);
        console.log('Collaborator Competence:', collaboratorCompetence);

        if (collaboratorCompetence) {
          collaboratorData.push(this.getCompetenceLevel(collaboratorCompetence));
        } else {
          collaboratorData.push(0); // If no matching competence is found, default to 0
        }
      });

      this.radarChartLabels = labels;
      this.radarChartData[0].data = requiredData;
      this.radarChartData[1].data = collaboratorData;

      console.log('Labels:', labels); // Check if the labels match your competences
      console.log('Required Data:', this.radarChartData[0].data); // Required competences from the poste
      console.log('Collaborator Data:', this.radarChartData[1].data); // Competences from the collaborator

      // Trigger chart update
      if (this.chart) {
        this.chart.update();
      }
    }
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
