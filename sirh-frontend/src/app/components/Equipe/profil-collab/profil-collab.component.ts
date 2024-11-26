import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CollaboratorService } from '../../../_services/collaborator.service';
import { PosteService } from '../../../_services/poste.service';
import { CompetenceService } from '../../../_services/competence.service';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-profil-collab',
  templateUrl: './profil-collab.component.html',
  styleUrls: ['./profil-collab.component.scss'],
})
export class ProfilCollabComponent implements OnInit {
  collaborator: any = null;
  poste: any = null;
  availableCompetences: any[] = [];
  combinedCompetences: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private collaboratorService: CollaboratorService,
    private posteService: PosteService,
    private competenceService: CompetenceService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const collaboratorId = Number(this.route.snapshot.paramMap.get('id'));
    if (collaboratorId) {
      this.getCollaboratorInfo(collaboratorId);
    }
  }

  getCollaboratorInfo(id: number): void {
    this.collaboratorService.findById(id).subscribe(
      (data) => {
        this.collaborator = data;

        if (this.collaborator?.posteOccupe && this.collaborator.posteOccupe.idPoste) {
          this.getPosteInfo(this.collaborator.posteOccupe.idPoste);
        } else {
          this.poste = null;
        }

        this.fetchAvailableCompetences();
      },
      (error) => {
        console.error('Error fetching collaborator details', error);
        this.collaborator = null;
      }
    );
  }

  private getPosteInfo(idPoste: number): void {
    this.posteService.getPosteById(idPoste).subscribe(
      (data) => {
        this.poste = data;
        this.generateCombinedCompetences();
      },
      (error) => {
        console.error('Error fetching poste details', error);
        this.poste = null;
      }
    );
  }

  fetchAvailableCompetences(): void {
    if (!this.collaborator) return;

    this.competenceService.CurrentUserCompetence(this.collaborator.id).subscribe(
      (response) => {
        this.availableCompetences = response;
        this.generateCombinedCompetences();
      },
      (error) => {
        console.error('Error fetching available competences: ', error);
      }
    );
  }

  generateCombinedCompetences(): void {
    if (!this.availableCompetences || !this.poste) return;

    const competenceMap = new Map<string, any>();

    // Map collaborator competences
    this.availableCompetences.forEach((comp: any) => {
      const name = this.mapCompetenceName(comp.competence.name);
      competenceMap.set(name, {
        name: name,
        collaboratorEvaluation: comp.evaluation,
        collaboratorScaleType: comp.scaleType,
        possibleValues: comp.competence.possibleValues,
        collaboratorCompetenceId: comp.competence.id,
        competenceId: comp.competence.id,
      });
    });

    // Map poste competences
    this.poste.requiredCompetence.forEach((comp: any) => {
      const name = this.mapCompetenceName(comp.competence.name);
      if (competenceMap.has(name)) {
        const existing = competenceMap.get(name);
        existing.posteEvaluation = comp.evaluation;
        existing.posteScaleType = comp.scaleType;
      } else {
        competenceMap.set(name, {
          name: name,
          collaboratorEvaluation: null,
          collaboratorScaleType: null,
          possibleValues: null,
          collaboratorCompetenceId: null,
          competenceId: comp.competence.id,
          posteEvaluation: comp.evaluation,
          posteScaleType: comp.scaleType,
        });
      }
    });

    this.combinedCompetences = Array.from(competenceMap.values());
    this.cdr.detectChanges();
  }

  onCollaboratorEvaluationChange(competence: any, newEvaluation: string): void {
    this.updateEvaluation(this.collaborator.id, competence.competenceId, newEvaluation);
    competence.collaboratorEvaluation = newEvaluation;

    // Update the local competenceAcquise data
    const competenceIndex = this.collaborator.competenceAcquise.findIndex(
      (c: any) => c.competence.id === competence.competenceId
    );
    if (competenceIndex !== -1) {
      this.collaborator.competenceAcquise[competenceIndex].evaluation = newEvaluation;
      this.collaborator.competenceAcquise = [...this.collaborator.competenceAcquise];
    }
    this.cdr.detectChanges();
  }

  updateEvaluation(collaboratorId: number, competenceId: number, evaluation: string): void {
    this.competenceService.updateCollaboratorEvaluation(collaboratorId, competenceId, evaluation).subscribe(
      () => {
        console.log(`Evaluation for competence ${competenceId} updated successfully.`);
      },
      (error: HttpErrorResponse) => {
        console.error('Error updating evaluation:', error);
      }
    );
  }

  mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      English: 'Anglais',
      French: 'Français',
      JAVA: 'Java',
      'Soft Skills': 'Compétences Douces',
      // Add more mappings as necessary
    };
    return competenceMap[name] || name;
  }

  mapEvaluationValue(value: string | null | undefined): string {
    if (!value) {
      return ''; // or return a default value
    }
    const evaluationMap: { [key: string]: string } = {
      '1 STAR': '1 Étoile',
      '2 STARS': '2 Étoiles',
      '3 STARS': '3 Étoiles',
      '4 STARS': '4 Étoiles',
      '5 STARS': '5 Étoiles',
      'EXCELLENT': 'Excellent',
      'BON': 'Bon',
      'MOYEN': 'Moyen',
      'FAIBLE': 'Faible',
    };
    return evaluationMap[value.toUpperCase()] || value;
  }



  getStarsArray(evaluation: string): number[] {
    const numStars = parseInt(evaluation.split(' ')[0], 10);
    return Array(numStars).fill(0);
  }

  getNumericValue(evaluation: string): number {
    const maxScale = 5; // Adjust according to your maximum scale
    const value = parseInt(evaluation, 10);
    return (value / maxScale) * 100; // Convert to percentage
  }

  generateAvatarUrl(firstname: string, lastname: string): string {
    const seed = firstname + lastname; // Use the name to create consistent avatars
    return `https://avatars.dicebear.com/api/human/${seed}.svg?mood[]=happy`;
  }

  getNumericLevel(evaluation: string): number {
    const value = parseInt(evaluation, 10);
    if (isNaN(value)) return 0;
    return value;
  }
}
