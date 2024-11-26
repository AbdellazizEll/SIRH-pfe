import { Component, OnInit, ViewChild } from '@angular/core';
import { CatalogueServiceService } from "../../../../_services/catalogue-service.service";
import { FormationsServiceService } from "../../../../_services/formations-service.service";
import { Router } from "@angular/router";
import { CompetenceService } from "../../../../_services/competence.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { NgForm } from '@angular/forms'; // Add this import

@Component({
  selector: 'app-ajout-formation',
  templateUrl: './ajout-formation.component.html',
  styleUrls: ['./ajout-formation.component.scss']
})
export class AjoutFormationComponent implements OnInit {
  @ViewChild('competenceModal') competenceModal: any;
  @ViewChild('formationForm') formationForm: NgForm; // ViewChild for accessing form reference

  newFormation: any = {
    title: '',
    description: '',
    targetCompetence: { id: null },
    currentLevel: 1,
    targetLevel: 1,
    startingAt: '',
    finishingAt: '',
    catalogue: { id: null }
  };

  tomorrow: string = '';
  catalogues: any[] = [];
  competencies: any[] = [];

  successMessage: string = '';
  errorMessage: string = '';
  formSubmitted: boolean = false; // Track form submission status

  constructor(
    private competenceService: CompetenceService,
    private formationService: FormationsServiceService,
    private catalogueService: CatalogueServiceService,
    private router: Router,
    private modalService: NgbModal,
  ) { }

  ngOnInit(): void {
    this.getAllCompetencies();
    this.getAllCatalogues();
    this.setTomorrowDate();
  }

  addFormation() {
    this.formSubmitted = true;
    this.successMessage = '';
    this.errorMessage = '';

    // Check if form is valid before proceeding with submission
    if (this.formationForm.invalid) {
      this.errorMessage = 'Veuillez remplir tous les champs requis.';
      return;
    }

    this.formationService.createFormation(this.newFormation).subscribe(
      (response) => {
        console.log("New formation added:", this.newFormation);
        this.successMessage = 'Formation ajoutée avec succès !';
        this.errorMessage = '';

        this.resetForm(); // Reset form on success
      },
      (error) => {
        this.errorMessage = 'Erreur lors de l\'ajout de la formation.';
        console.error(error);
        this.successMessage = '';
      }
    );
  }

  onStartDateChange(): void {
    if (this.newFormation.finishingAt && this.newFormation.finishingAt < this.newFormation.startingAt) {
      this.newFormation.finishingAt = '';
    }
  }

  private setTomorrowDate() {
    const today = new Date();
    today.setDate(today.getDate() + 1);
    this.tomorrow = today.toISOString().split('T')[0];
  }

  private getAllCompetencies() {
    this.competenceService.getAllCompetences().subscribe(
      (response) => {
        this.competencies = Array.isArray(response.data?.page?.content) ? response.data.page.content : [];
        console.log("Competencies:", this.competencies);
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement des compétences.';
        console.error(error);
      }
    );
  }

  private getAllCatalogues() {
    this.catalogueService.getAllCatalogues().subscribe(
      (response) => {
        this.catalogues = Array.isArray(response.data?.catalogues) ? response.data.catalogues : [];
        console.log("Catalogues:", this.catalogues);
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement des catalogues.';
        console.error(error);
      }
    );
  }

  openCompetenceModal() {
    this.modalService.open(this.competenceModal);
  }

  onCompetenceChange(event: Event) {
    const selectedValue = (event.target as HTMLSelectElement).value;
    if (selectedValue === 'add-new') {
      this.openCompetenceModal();
      (event.target as HTMLSelectElement).selectedIndex = 0;
    }
  }

  onCompetenceAdded(competence: any) {
    this.modalService.dismissAll();
    this.competencies.push(competence);
  }

  closeCompetenceModal() {
    this.modalService.dismissAll();
  }

  onCancel() {
    this.router.navigate(["/ListDesFormations"]);
  }

  private resetForm() {
    this.newFormation = {
      title: '',
      description: '',
      targetCompetence: { id: null },
      currentLevel: 1,
      targetLevel: 1,
      startingAt: '',
      finishingAt: '',
      catalogue: { id: null }
    };
    this.formSubmitted = false; // Reset form submission status

    if (this.formationForm) {
      this.formationForm.resetForm(); // Use Angular's reset method to reset the form
    }
  }
}
