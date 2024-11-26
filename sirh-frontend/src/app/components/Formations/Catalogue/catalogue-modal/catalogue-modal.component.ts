import { Component, Input } from '@angular/core';
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-catalogue-modal',
  templateUrl: './catalogue-modal.component.html',
  styleUrls: ['./catalogue-modal.component.scss']
})
export class CatalogueModalComponent {

  @Input() isEditMode: boolean = false;
  @Input() catalogueTitle: string = '';
  @Input() catalogueDescription: string = '';
  @Input() selectedFormations: any[] = [];
  @Input() availableFormations: any[] = [];
  errorMessage: string = '';

  constructor(public activeModal: NgbActiveModal) {}

  submitForm() {
    console.log('Catalogue Title:', this.catalogueTitle);
    console.log('Catalogue Description:', this.catalogueDescription);
    console.log('Selected Formations:', this.selectedFormations);

    // Validate selected formations are part of available formations
    const validFormations = this.selectedFormations.every(id =>
      this.availableFormations.some(formation => formation.id === id)
    );

    if (!validFormations) {
      this.errorMessage = 'Invalid formation selection. Please select valid formations.';
      return;
    }

    if (this.catalogueTitle && this.catalogueDescription) {
      const catalogueData = {
        title: this.catalogueTitle,
        description: this.catalogueDescription,
        formations: this.selectedFormations
      };
      console.log('Submitting Catalogue Data:', catalogueData); // Debugging log
      this.activeModal.close(catalogueData);
    }
  }

  closeModal() {
    this.activeModal.dismiss('Cross click');
  }
}
