import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CatalogueModalComponent } from '../catalogue-modal/catalogue-modal.component';
import {CatalogueServiceService} from "../../../../_services/catalogue-service.service";
import {FormationsServiceService} from "../../../../_services/formations-service.service";
import {ConfirmModalComponent} from "../confirm-modal/confirm-modal.component";
import {Router} from "@angular/router";
@Component({
  selector: 'app-gerer-catalogue',
  templateUrl: './gerer-catalogue.component.html',
  styleUrls: ['./gerer-catalogue.component.scss']
})
export class GererCatalogueComponent implements OnInit {
  catalogues: any[] = [];
  availableFormations: any = [];
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private catalogueService: CatalogueServiceService,
    private formationService: FormationsServiceService,
    private modalService: NgbModal,
    private router:Router,
    private cdr: ChangeDetectorRef


  ) {}

  ngOnInit(): void {
    this.loadCatalogues();
    this.loadFormations();
  }

  openAddModal() {
    const modalRef = this.modalService.open(CatalogueModalComponent);
    modalRef.componentInstance.isEditMode = false;
    modalRef.componentInstance.availableFormations = this.availableFormations;

    modalRef.result.then(
      (catalogueData) => {
        if (catalogueData) {
          this.createCatalogue(catalogueData);
        }
      },
      (reason) => {
        console.log('Modal dismissed:', reason);
      }
    );
  }

  editCatalogue(catalogue: any, event:Event) {
    event.stopPropagation(); // Prevent the row click event from being triggered

    const modalRef = this.modalService.open(CatalogueModalComponent);
    modalRef.componentInstance.isEditMode = true;
    modalRef.componentInstance.catalogueTitle = catalogue.title;
    modalRef.componentInstance.catalogueDescription = catalogue.description;
    modalRef.componentInstance.selectedFormations = catalogue.formations.map((f: any) => f.id);
    modalRef.componentInstance.availableFormations = this.availableFormations;

    modalRef.result.then(
      (catalogueData) => {
        if (catalogueData) {
          console.log('Catalogue Data to Update:', catalogueData); // Debugging log
          this.updateCatalogue(catalogue.id, catalogueData);
        }
      },
      (reason) => {
        console.log('Modal dismissed:', reason);
      }
    );
  }

  // Load catalogues
  loadCatalogues() {
    this.catalogueService.getAllCatalogues().subscribe(
      (response) => {
        this.catalogues = response.data.catalogues;
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement des catalogues.';
      }
    );
  }

  // Load formations
  loadFormations() {
    this.formationService.getAllFormations().subscribe(
      (response) => {
        this.availableFormations = response.data;
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement des formations.';
      }
    );
  }

  // Create new catalogue
  createCatalogue(catalogueData: any) {
    this.catalogueService.createCatalogue(catalogueData).subscribe(
      (response) => {
        this.successMessage = 'Catalogue créé avec succès.';
        this.loadCatalogues();
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la création du catalogue.';
      }
    );
  }

  // Update existing catalogue
  updateCatalogue(id: number, catalogueData: any) {

    event.stopPropagation(); // Prevent the row click event from being triggered

    this.catalogueService.updateCatalogue(id, catalogueData).subscribe(
      (response) => {
        this.successMessage = 'Catalogue mis à jour avec succès.';
        this.loadCatalogues();
        this.cdr.detectChanges(); // Force Angular to detect changes
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la mise à jour du catalogue.';
      }
    );
  }

  // Delete a catalogue
    deleteCatalogue(id: number, event: Event) {
      event.stopPropagation(); // Prevent the row click event from being triggered

      const modalRef = this.modalService.open(ConfirmModalComponent);
    modalRef.componentInstance.message = 'Êtes-vous sûr de vouloir supprimer ce catalogue ?';

    modalRef.result.then(
      (confirmed) => {
        if (confirmed) {
          this.catalogueService.deleteCatalogue(id).subscribe(
            (response) => {
              this.successMessage = 'Catalogue supprimé avec succès.';
              this.loadCatalogues();
            },
            (error) => {
              this.errorMessage = 'Erreur lors de la suppression du catalogue.';
            }
          );
        }
      },
      (reason) => {
        console.log('Modal dismissed:', reason);
      }
    );
  }

  redirectAddCatalogue() {
  this.router.navigate(["CatalogueFormation"])
  }

  goToCatalogue(id:number) {
    this.router.navigate(['ViewCatalogue',id])
  }
}
