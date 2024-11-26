import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CatalogueServiceService} from "../../../../_services/catalogue-service.service";
import {DemandeFormationServiceService} from "../../../../_services/demande-formation-service.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-demande-modifier',
  templateUrl: './demande-modifier.component.html',
  styleUrls: ['./demande-modifier.component.scss']
})
export class DemandeModifierComponent implements OnInit {
  modifyForm!: FormGroup;
  catalogue: any[] = [];
  formations: any[] = [];
  selectedRequest: any = null;
  errorMessage = '';
  successMessage = '';

  catalogueSelected = false;  // To track if catalogue is selected


  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    private catalogueService: CatalogueServiceService,
    private demandeFormationService: DemandeFormationServiceService,
    private modalService: NgbModal
  ) {
    if (data && data.selectedRequest) {
      this.selectedRequest = data.selectedRequest;
      this.populateForm(this.selectedRequest);
    }
  }

  ngOnInit(): void {
    this.modifyForm = this.fb.group({
      catalogue: ['', Validators.required],  // Catalogue control added here
      formationTitle: [{ value: '', disabled: true }, Validators.required],  // The formation title, initially disabled
      justification: ['', Validators.required]  // Justification is required
    });

  }

  submitModifiedRequest(): void {
    if (this.modifyForm.valid) {
      const modifiedRequest = {
        justification: this.modifyForm.get('justification')?.value,
        formationId: this.modifyForm.get('formationTitle')?.value
      };

      // Call the service to update the request (assuming you pass the ID as well)
      this.demandeFormationService.updateRequest(this.selectedRequest.id, modifiedRequest).subscribe(
        response => {
          this.successMessage = 'Demande modifiée avec succès';
          this.modalService.dismissAll(); // Close the modal after successful update
        },
        error => {
          this.errorMessage = 'Erreur lors de la modification de la demande';
          console.error('Error updating request:', error);
        }
      );
    }
  }

  onCatalogueChange(): void {
    const catalogueId = this.modifyForm.get('catalogue')?.value;  // Get selected catalogueId

    if (catalogueId) {
      this.catalogueSelected = true;
      this.catalogueService.getTrainingsByCatalogue(catalogueId).subscribe(
        (response: any) => {
          console.log("Full response from API: ", response);  // Log the full response

          // Access formations from the path: response.data[""].content
          if (response?.data?.[""]?.content && Array.isArray(response.data[""].content)) {
            this.formations = response.data[""].content;  // Assign formations array
          } else {
            console.error('Expected an array, but got:', typeof response.data);
            this.formations = [];  // Handle invalid format or missing data
          }

          this.modifyForm.get('formationTitle')?.enable();  // Enable the formation dropdown
        },
        (error: any) => {
          this.errorMessage = 'Erreur lors du chargement des formations';
          console.error('Error loading formations:', error);
          this.formations = [];  // Ensure it's an array even in case of error
        }
      );
    } else {
      this.catalogueSelected = false;
      this.formations = [];
      this.modifyForm.get('formationTitle')?.disable();  // Disable the formation dropdown if no catalogue is selected
    }
  }

  populateForm(request: any): void {
    if (request) {
      this.modifyForm.patchValue({
        catalogue: request?.formation?.catalogue?.id || '',
        formationTitle: request?.formation?.id || '',
        justification: request?.justification || ''
      });

      // Load formations based on selected catalogue
      if (request?.formation?.catalogue?.id) {
        this.onCatalogueChange();
      }
    }
  }

}
