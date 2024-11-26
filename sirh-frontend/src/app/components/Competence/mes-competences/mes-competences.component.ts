import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { CollaboratorService } from "../../../_services/collaborator.service";
import { PosteService } from "../../../_services/poste.service";
import { TokenStorageService } from "../../../_services/token-storage.service";

@Component({
  selector: 'app-mes-competences',
  templateUrl: './mes-competences.component.html',
  styleUrls: ['./mes-competences.component.scss']
})
export class MesCompetencesComponent implements OnInit {

  collaborator: any = null;
  poste: any = null;
  userInfo: any;
  combinedCompetences: any[] = [];
  availableCompetences: any[] = [];
  isEvaluationEditable: boolean = false; // Set to true if evaluations should be editable
  avatarUrl: string = ''; // New variable to store avatar URL

  // Local assets for avatars
  avatarAssets: string[] = [
    '/assets/Avatars/asset1.jpg',
    '/assets/Avatars/asset2.jpg',
    '/assets/Avatars/asset3.jpg',
    '/assets/Avatars/asset4.jpg',
    '/assets/Avatars/asset5.jpg',
    '/assets/Avatars/asset6.jpg',
  ];

  constructor(
    private route: ActivatedRoute,
    private collaboratorService: CollaboratorService,
    private posteService: PosteService,
    private tokenStorage: TokenStorageService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.userInfo = this.tokenStorage.getUser();
    const collaboratorId = Number(this.route.snapshot.paramMap.get('id'));
    if (collaboratorId) {
      this.getCollaboratorInfo(collaboratorId);
    }
  }

  getCollaboratorInfo(id: number) {
    this.collaboratorService.findById(id).subscribe(
      data => {
        this.collaborator = data;
        this.poste = null;
        this.availableCompetences = this.collaborator?.competenceAcquise || [];
        // Assign an avatar from the local assets after fetching collaborator data
        this.avatarUrl = this.getAvatarUrl(this.collaborator.id);

        if (this.collaborator?.posteOccupe?.idPoste) {
          this.getPosteInfo(this.collaborator.posteOccupe.idPoste);
        } else {
          this.combinedCompetences = [];
        }
      },
      error => {
        console.error('Error fetching collaborator details', error);
        this.collaborator = null;
        this.poste = null;
        this.combinedCompetences = [];
      }
    );
  }

  private getPosteInfo(idPoste: number) {
    this.posteService.getPosteById(idPoste).subscribe(
      data => {
        this.poste = data;
        this.generateCombinedCompetences();
      },
      error => {
        console.error('Error fetching poste details', error);
        this.poste = null;
        this.combinedCompetences = [];
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
        competenceId: comp.competence.id,
      });
    });

    // Map poste competences
    this.poste.requiredCompetence.forEach((comp: any) => {
      const name = this.mapCompetenceName(comp.competence.name);
      const existing = competenceMap.get(name);
      if (existing) {
        existing.posteEvaluation = comp.evaluation;
        existing.posteScaleType = comp.scaleType;
      } else {
        competenceMap.set(name, {
          name: name,
          collaboratorEvaluation: null,
          collaboratorScaleType: null,
          possibleValues: null,
          competenceId: comp.competence.id,
          posteEvaluation: comp.evaluation,
          posteScaleType: comp.scaleType,
        });
      }
    });

    this.combinedCompetences = Array.from(competenceMap.values());
    this.cdr.detectChanges();
  }

  mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      English: 'Anglais',
      French: 'Français',
      JAVA: 'Java',
      'Soft Skills': 'Compétences Douces',
      // Add additional mappings as needed
    };
    return competenceMap[name] || name;
  }

  mapEvaluationValue(value: string | null | undefined): string {
    if (!value) return '';
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
    const maxScale = 5;
    const value = parseInt(evaluation, 10);
    return (value / maxScale) * 100;
  }

  onCollaboratorEvaluationChange(competence: any, value: any) {
    // Placeholder for future logic on collaborator evaluation change
  }

  getAvatarUrl(collaboratorId: number): string {
    // Assign avatar using the collaborator ID to cycle through available avatars
    const index = collaboratorId % this.avatarAssets.length;
    return this.avatarAssets[index];
  }
}
