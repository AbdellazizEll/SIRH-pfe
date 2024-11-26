import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormationsServiceService } from '../../../../_services/formations-service.service';
import { CompetenceService } from '../../../../_services/competence.service';
import { CatalogueServiceService } from '../../../../_services/catalogue-service.service';

@Component({
  selector: 'app-modifier-formation-modal',
  templateUrl: './modifier-formation-modal.component.html',
  styleUrls: ['./modifier-formation-modal.component.scss']
})
export class ModifierFormationModalComponent implements OnInit {
  trainingForm: FormGroup;
  isEditMode = false;

  @Input() training: any;
  competenceList: any[] = [];  // List of competencies
  catalogueList: any[] = [];   // List of catalogues
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ModifierFormationModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private formationService: FormationsServiceService,
    private competenceService: CompetenceService,
    private catalogueService: CatalogueServiceService
  ) {}

  ngOnInit(): void {
    this.isEditMode = !!this.data.id;

    // Initialize form with form controls
    this.trainingForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      targetCompetence: ['', Validators.required],  // Storing selected competence id directly
      currentLevel: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      targetLevel: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      startingAt: ['', Validators.required],
      finishingAt: ['', Validators.required],
      catalogue: ['', Validators.required]  // Storing selected catalogue id directly
    });

    // Load competences and catalogues from their respective services
    this.loadCompetences();
    this.loadCatalogues();

    // If edit mode, load training details
    if (this.isEditMode) {
      this.loadTrainingDetails();
    }
  }

  // Load competences list for the dropdown
  loadCompetences(): void {
    this.competenceService.getAllCompetences().subscribe(
      (response) => {
        console.log('Competences response:', response);
        this.competenceList = Array.isArray(response.data?.page?.content) ? response.data.page.content : [];
      },
      (error) => {
        console.error('Error loading competences:', error);
      }
    );
  }

  // Load catalogues list for the dropdown
  loadCatalogues(): void {
    this.catalogueService.getAllCatalogues().subscribe(
      (response) => {
        console.log('Catalogues response:', response);
        this.catalogueList = Array.isArray(response.data?.catalogues) ? response.data.catalogues : [];
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement des catalogues.';
        console.error('Error loading catalogues:', error);
      }
    );
  }

  // Load training details if we are in edit mode
  loadTrainingDetails(): void {
    console.log('Editing formation with data:', this.data);
    this.formationService.getFormationById(this.data.id).subscribe(
      (response) => {
        const formation = response.data.formation;
        console.log('Formation data:', formation);

        // Patch form values and ensure `targetCompetence` is treated as a number
        this.trainingForm.patchValue({
          title: formation.title || '',
          description: formation.description || '',
          targetCompetence: Number(formation.targetCompetence?.id) || '',  // Set competence id as number
          currentLevel: formation.currentLevel || '',
          targetLevel: formation.targetLevel || '',
          startingAt: formation.startingAt || '',
          finishingAt: formation.finishingAt || '',
          catalogue: formation.catalogue?.id || ''  // Set catalogue id
        });

        // Log the form values after patching to verify
        console.log('Form Values After Patch:', this.trainingForm.getRawValue());
      },
      (error) => {
        console.error('Error loading training details', error);
      }
    );
  }

  // Function to map competence name from English to French
  mapCompetenceName(name: string): string {
    const competenceMap: { [key: string]: string } = {
      'English': 'Anglais',
      'French': 'Français',
      'JAVA': 'Java',
      'Soft Skills': 'Compétences Douces'
    };
    return competenceMap[name] || name;
  }

  // Function to handle submission
  onSubmit(): void {
    if (this.trainingForm.valid) {
      const updatedTraining = {
        id: this.data.id,
        title: this.trainingForm.value.title,
        description: this.trainingForm.value.description,
        targetCompetence: {
          id: this.trainingForm.value.targetCompetence
        },
        currentLevel: this.trainingForm.value.currentLevel,
        targetLevel: this.trainingForm.value.targetLevel,
        startingAt: this.trainingForm.value.startingAt,
        finishingAt: this.trainingForm.value.finishingAt,
        catalogue: {
          id: this.trainingForm.value.catalogue
        }
      };

      console.log('Updating Training:', updatedTraining);

      if (updatedTraining.id) {
        this.formationService.updateFormation(updatedTraining.id, updatedTraining).subscribe(
          () => {
            this.successMessage = "La formation a été mise à jour avec succès.";
            setTimeout(() => this.successMessage = '', 3000);
            this.dialogRef.close('updated');
          },
          (err) => {
            console.error('Error updating training:', err);
            this.errorMessage = 'Erreur lors de la mise à jour de la formation.';
            setTimeout(() => this.errorMessage = '', 3000);
          }
        );
      }
    } else {
      console.log('Form is invalid:', this.trainingForm.errors);
    }
  }

  // Close the modal without saving
  close(): void {
    this.dialogRef.close();
  }

  // Track by function for competence list
  trackByCompetence(index: number, competence: any): number {
    return competence.id;
  }
}
