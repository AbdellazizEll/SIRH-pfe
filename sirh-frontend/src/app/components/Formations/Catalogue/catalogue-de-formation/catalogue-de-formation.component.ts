import { Component, OnInit } from '@angular/core';
import { CatalogueServiceService } from "../../../../_services/catalogue-service.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-catalogue-de-formation',
  templateUrl: './catalogue-de-formation.component.html',
  styleUrls: ['./catalogue-de-formation.component.scss']
})
export class CatalogueDeFormationComponent implements OnInit {
  catalogues: any[] = [];  // Holds all catalogues
  errorMessage: string = '';  // For displaying errors
  successMessage: string = '';  // For displaying success messages

  catalogueForm!: FormGroup;  // The form group for the catalogue form

  constructor(private catalogueService: CatalogueServiceService,
              private fb: FormBuilder,
              private router:Router) {}

  ngOnInit(): void {
    this.getAllCatalogues();

    // Initialize the form
    this.catalogueForm = this.fb.group({
      title: ['', [Validators.required]],  // Title is required
      description: ['', [Validators.required]]  // Description is required
    });
  }

  // Fetch all catalogues when component initializes
  getAllCatalogues(): void {
    this.catalogueService.getAllCatalogues().subscribe(
      (response) => {
        console.log(response);  // Check the structure of the response
        // Safely access catalogues array
        this.catalogues = response?.data?.catalogues || [];
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement des catalogues.';
      }
    );
  }

  // Create a new catalogue
  createCatalogue(): void {
    // Check if the form is valid
    if (this.catalogueForm.invalid) {
      this.catalogueForm.markAllAsTouched();
      return;
    }

    // Prepare the catalogue object from form values
    const newCatalogue = {
      title: this.catalogueForm.get('title')?.value,
      description: this.catalogueForm.get('description')?.value
    };

    console.log('Submitting new catalogue:', newCatalogue);  // Log the data being submitted

    // Call the service to create a new catalogue
    this.catalogueService.createCatalogue(newCatalogue).subscribe(
      (response) => {
        this.successMessage = 'Catalogue ajouté avec succès !';
        this.catalogues.push(response);  // Add the new catalogue to the list
        this.goToGererCatalogue();// Reset the form
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la création du catalogue.';
      }
    );
  }

  // View a specific catalogue
  viewCatalogue(catalogue: any): void {
    console.log('Catalogue selected:', catalogue);
    // Logic to navigate to catalogue details or fetch formations can be added here
  }

  // Method to navigate home

  onCancel() {
  this.router.navigate(["GererCatalogue"])
  }

  private goToGererCatalogue() {
    this.router.navigate(['/GererCatalogue'])
  }
}
